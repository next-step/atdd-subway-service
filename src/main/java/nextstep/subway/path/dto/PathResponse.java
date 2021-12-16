package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private int distance;
    private List<StationResponse> stations;

    public PathResponse() {
    }

    private PathResponse(final int distance, final List<StationResponse> stations) {
        this.distance = distance;
        this.stations = stations;
    }

    public static PathResponse from(Path path) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new PathResponse((int) path.getDistance(), stations);
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
}
