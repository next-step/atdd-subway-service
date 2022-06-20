package nextstep.subway.path.domain;

import java.util.Objects;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class JGraphPathFinder implements PathFindStrategy {

    @Override
    public Path searchShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
        Station source, Station target) {

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
            graph);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(source,
            target);
        validateResult(shortestPath);
        return Path.of(shortestPath.getVertexList(), (int) shortestPath.getWeight());
    }

    private void validateResult(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        if (Objects.isNull(shortestPath)) {
            throw new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }

}
