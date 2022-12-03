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
    private final int extraCharge;

    public StationPath(final List<String> stationNames, final int distance, final int extraCharge) {
        this.stationNames = stationNames;
        this.distance = distance;
        this.extraCharge = extraCharge;
    }

    public static StationPath of(GraphPath<String, SectionWeightedEdge> path, int extraCharge) {

        return new StationPath(path.getVertexList(), (int) path.getWeight(), extraCharge);
    }

    public List<String> getStationNames() {
        return stationNames;
    }

    public int getDistance() {
        return distance;
    }

    public int getExtraCharge() {
        return extraCharge;
    }

    @Override
    public String toString() {
        return "StationPath{" +
                "stationNames=" + stationNames +
                ", distance=" + distance +
                ", extraCharge=" + extraCharge +
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
