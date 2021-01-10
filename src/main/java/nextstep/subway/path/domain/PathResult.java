package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class PathResult {
    private GraphPath<Station, DefaultWeightedEdge> result;

    public PathResult(GraphPath<Station, DefaultWeightedEdge> result) {
        this.result = result;
    }

    public List<Station> getStations() {
        return result.getVertexList();
    }

    public int getTotalDistance() {
        return (int)result.getWeight();
    }
}
