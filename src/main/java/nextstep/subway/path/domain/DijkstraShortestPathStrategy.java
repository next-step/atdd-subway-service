package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Objects;

public class DijkstraShortestPathStrategy implements ShortestPathStrategy {
    @Override
    public Path findShortestPath(WeightedMultigraph<Station, SectionEdge> graph,
                                                                    Station source,
                                                                    Station target) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> shortestPath = dijkstraShortestPath.getPath(source, target);
        validateShortestPath(shortestPath);
        int additionalFare = findMaxAdditionalFare(shortestPath);
        return Path.of(shortestPath.getVertexList(), (int)shortestPath.getWeight(), additionalFare);
    }

    private Integer findMaxAdditionalFare(GraphPath<Station, SectionEdge> shortestPath) {
        return shortestPath.getEdgeList().stream()
                .map(SectionEdge::getLineAdditionalFare)
                .max(Integer::compareTo)
                .orElse(0);
    }

    private void validateShortestPath(GraphPath<Station, SectionEdge> shortestPath) {
        if (Objects.isNull(shortestPath)) {
            throw new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }
}
