package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.math.BigDecimal;
import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private BigDecimal fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public PathResponse(List<StationResponse> stations, int distance, BigDecimal fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(Path path) {
        return new PathResponse(path.getStationResponses(), path.findWeight());
    }

    public static PathResponse of(Path path, Fare fare) {
        return new PathResponse(path.getStationResponses(), path.findWeight(), fare.getFare());
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
}
