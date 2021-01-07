package nextstep.subway.path.domain;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStation;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Optional;

public class Path {
    private final WeightedMultigraph<PathStation, DefaultWeightedEdge> graph;
    private final PathStation source;
    private final PathStation target;

    public Path(WeightedMultigraph<PathStation, DefaultWeightedEdge> graph, PathStation source, PathStation target) {
        this.graph = graph;
        this.source = source;
        this.target = target;
    }

    public PathResponse ofResponse() {
        return Optional.ofNullable(getShortestPath())
                .map(shortestPath -> PathResponse.of(shortestPath.getVertexList(), (int) shortestPath.getWeight()))
                .orElseThrow(() -> new IllegalArgumentException("출발역과 도착역이 연결 되어 있지 않습니다."));
    }

    private GraphPath<PathStation, DefaultWeightedEdge> getShortestPath() {
        return new DijkstraShortestPath<>(this.graph)
                .getPath(this.source, this.target);
    }
}
