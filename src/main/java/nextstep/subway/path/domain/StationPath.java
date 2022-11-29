package nextstep.subway.path.domain;

import nextstep.subway.path.exception.PathException;
import nextstep.subway.path.exception.PathExceptionType;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.Objects;

public class StationPath {

    private final List<String> stationNames;
    private final int distance;

    public StationPath(final List<String> stationNames, final int distance) {
        this.stationNames = stationNames;
        this.distance = distance;
    }

    public static StationPath of(GraphPath<String, DefaultWeightedEdge> path) {
        if(Objects.isNull(path)){
            throw new PathException(PathExceptionType.NO_PATH);
        }
        return new StationPath(path.getVertexList(), (int) path.getWeight());
    }

    public List<String> getStationNames() {
        return stationNames;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "ShortestPathResponse{" +
                "stationNames=" + stationNames +
                ", distance=" + distance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationPath that = (StationPath) o;
        return distance == that.distance && Objects.equals(stationNames, that.stationNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationNames, distance);
    }
}
