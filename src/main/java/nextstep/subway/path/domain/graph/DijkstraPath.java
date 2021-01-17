package nextstep.subway.path.domain.graph;

import nextstep.subway.station.domain.Station;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class DijkstraPath implements PathAlgorithm {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public DijkstraPath(Graph<Station, DefaultWeightedEdge> stationGraph) {
        this.dijkstraShortestPath = new DijkstraShortestPath<>(stationGraph);
    }

    @Override
    public List<Station> getShortestPath(Station source, Station target) {
        return getPath(source, target).getVertexList();
    }

    @Override
    public int getDistance(Station source, Station target) {
        return (int) getPath(source, target).getWeight();
    }

    @Override
    public GraphPath<Station, DefaultWeightedEdge> getPath(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target);
    }
}
