package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.Objects;

public class PathResponse {

    private final List<Station> list;
    private final int distance;

    public PathResponse(Path shortestPath) {
        this.list = shortestPath.stations();
        this.distance = shortestPath.distance();
    }

    public static PathResponse of(Path shortestPath) {
        return new PathResponse(shortestPath);
    }

    public List<Station> getList() {
        return list;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathResponse that = (PathResponse) o;
        return distance == that.distance && Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list, distance);
    }

    @Override
    public String toString() {
        return "PathResponse{" +
                "list=" + list +
                ", distance=" + distance +
                '}';
    }
}
