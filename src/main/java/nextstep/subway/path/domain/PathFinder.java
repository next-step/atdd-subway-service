package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;


public class PathFinder {
    private DijkstraShortestPath dijkstraShortestPath;


    private PathFinder(DijkstraShortestPath dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static PathFinder initialPathFinder(List<Line> lines) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        lines.forEach(line ->
            line.getSectionList().forEach(section -> {
                graph.addVertex(section.getUpStation().getId());
                graph.addVertex(section.getDownStation().getId());
                graph.setEdgeWeight(
                        graph.addEdge(section.getUpStation().getId(), section.getDownStation().getId()), section.getDistance()
                );
            })
        );
        return new PathFinder(new DijkstraShortestPath(graph));
    }

    public GraphPath getShortestPath(Long sourceId, Long targetId) {
        GraphPath path =dijkstraShortestPath.getPath(sourceId, targetId);
        if(Objects.isNull(path)) {
            throw new IllegalArgumentException("경로를 찾지 못하였습니다.");
        }
        return path;
    }
}
