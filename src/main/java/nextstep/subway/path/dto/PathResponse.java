package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private BigDecimal fare;

    private PathResponse() {
    }

    private PathResponse(List<StationResponse> stations, int distance, BigDecimal fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(Path path, Fare fare) {
        List<StationResponse> stationResponses = toStationResponses(path);
        return new PathResponse(stationResponses, path.getDistance().value(), fare.value());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public BigDecimal getFare() {
        return fare;
    }

    private static List<StationResponse> toStationResponses(Path path) {
        return path.getStations()
                .stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }
}
