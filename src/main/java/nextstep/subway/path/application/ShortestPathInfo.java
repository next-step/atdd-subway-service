package nextstep.subway.path.application;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class ShortestPathInfo {
    private GraphPath<Station, DefaultWeightedEdge> path;

    public ShortestPathInfo(GraphPath<Station, DefaultWeightedEdge> path) {
        this.path = path;
    }

    public List<Station> getResultStations() {
        return path.getVertexList();
    }

    public Integer getShortestDistance() {
        return (int)path.getWeight();
    }
}
