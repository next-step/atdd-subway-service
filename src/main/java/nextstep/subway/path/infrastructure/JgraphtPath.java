package nextstep.subway.path.infrastructure;

import java.util.Collection;
import java.util.List;
import nextstep.subway.common.exception.ErrorCode;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.path.application.PathFactory;
import nextstep.subway.path.dto.PathEdge;
import nextstep.subway.path.dto.PathResult;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class JgraphtPath implements PathFactory {

    private final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
        DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
        graph);


    @Override
    public PathResult findShortestPath(List<PathEdge> pathEdges, Long source, Long target) {
        graphMapping(pathEdges);

        return getPathSearchResult(source, target);
    }

    private PathResult getPathSearchResult(Long source, Long target) {
        GraphPath<Long, DefaultWeightedEdge> graphPath = getGraphPath(source, target);

        List<Long> result = getVertexList(graphPath);
        int weight = (int) graphPath.getWeight();
        return PathResult.of(result, weight);
    }

    private void graphMapping(List<PathEdge> pathEdges) {
        pathEdges.stream()
            .map(PathEdge::getAllVertex)
            .distinct()
            .flatMap(Collection::stream)
            .forEach(graph::addVertex);

        pathEdges
            .forEach(this::setEdgeWeight);
    }

    private void setEdgeWeight(PathEdge pathEdge) {
        graph.setEdgeWeight(graph.addEdge(pathEdge.getSourceVertex(), pathEdge.getTargetVertex()),
            pathEdge.getWeight());
    }

    private GraphPath<Long, DefaultWeightedEdge> getGraphPath(Long source, Long target) {
        return dijkstraShortestPath.getPath(source, target);
    }

    private List<Long> getVertexList(GraphPath<Long, DefaultWeightedEdge> graphPath) {
        try {
            return graphPath.getVertexList();
        } catch (NullPointerException nullPointerException) {
            throw InvalidParameterException.of(ErrorCode.PATH_NOT_CONNECT);
        }
    }
}
