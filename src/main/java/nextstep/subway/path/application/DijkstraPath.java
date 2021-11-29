package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathResult;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;

public class DijkstraPath implements Path {

    private final DijkstraShortestPath dijkstra;

    public DijkstraPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        dijkstra = new DijkstraShortestPath(graph);
    }

    @Override
    public PathResult find(Station source, Station target) {
        GraphPath path = dijkstra.getPath(source, target);
        List<Station> shortestPath = path.getVertexList();
        int totalDistance = (int) path.getWeight();
        return new PathResult(shortestPath, totalDistance);
    }
}
