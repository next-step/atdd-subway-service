package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathResponse {

    private final List<StationResponse> stationResponses;
    private final int distance;

    public PathResponse(Path shortestPath) {
        this.stationResponses = shortestPath.stations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        this.distance = shortestPath.distance();
    }

    public static PathResponse of(Path shortestPath) {
        return new PathResponse(shortestPath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathResponse that = (PathResponse) o;
        return distance == that.distance && Objects.equals(stationResponses, that.stationResponses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationResponses, distance);
    }

    @Override
    public String toString() {
        return "PathResponse{" +
                "list=" + stationResponses +
                ", distance=" + distance +
                '}';
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public int getDistance() {
        return distance;
    }
}
