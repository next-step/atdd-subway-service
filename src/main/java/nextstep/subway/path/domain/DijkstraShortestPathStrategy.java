package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Objects;

public class DijkstraShortestPathStrategy implements ShortestPathStrategy {
    @Override
    public Path findShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                                                                    Station source,
                                                                    Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(source, target);
        validateShortestPath(shortestPath);
        return Path.of(shortestPath.getVertexList(), (int)shortestPath.getWeight());
    }

    private void validateShortestPath(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        if (Objects.isNull(shortestPath)) {
            throw new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }
}
