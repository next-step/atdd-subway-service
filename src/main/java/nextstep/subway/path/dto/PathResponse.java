package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private Integer distance;
    private BigDecimal fare;
    private List<StationResponse> stations;

    public PathResponse() {
    }

    private PathResponse(final Integer distance, final BigDecimal fare, final List<StationResponse> stations) {
        this.distance = distance;
        this.fare = fare;
        this.stations = stations;
    }

    public static PathResponse of(final Path path, final Fare fare) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new PathResponse(path.getDistance().getValue(), fare.value(), stations);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public void setStations(final List<StationResponse> stations) {
        this.stations = stations;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(final int distance) {
        this.distance = distance;
    }

    public BigDecimal getFare() {
        return fare;
    }
}
