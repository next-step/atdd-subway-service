package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.Objects;

public class StationPath {

    private final List<Station> stations;
    private final int distance;

    public StationPath(final List<Station> stations, final int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static StationPath of(GraphPath<Station, DefaultWeightedEdge> path) {
        return new StationPath(path.getVertexList(), (int) path.getWeight());
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "ShortestPathResponse{" +
                "stations=" + stations +
                ", distance=" + distance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationPath that = (StationPath) o;
        return distance == that.distance && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }
}
