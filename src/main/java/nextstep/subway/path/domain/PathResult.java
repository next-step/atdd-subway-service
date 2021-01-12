package nextstep.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class PathResult {
    private GraphPath<Long, DefaultWeightedEdge> result;

    public PathResult(GraphPath<Long, DefaultWeightedEdge> result) {
        this.result = result;
    }

    public List<Long> getStationIds() {
        return result.getVertexList();
    }

    public int getTotalDistance() {
        return (int)result.getWeight();
    }
}
