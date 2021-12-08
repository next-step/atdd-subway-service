package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraAlgorithm implements ShortestPathAlgorithm {
    private DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public void init(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public GraphPath<Station, DefaultWeightedEdge> getPath(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target);
    }
}
