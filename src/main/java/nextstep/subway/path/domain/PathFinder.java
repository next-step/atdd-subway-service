package nextstep.subway.path.domain;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import nextstep.subway.path.exception.NotContainVertexException;
import nextstep.subway.path.exception.NoConnectedVertexesException;

public class PathFinder {
    private final Graph graph;
    private final DijkstraShortestPath pathMaker;

    private PathFinder(Graph graph, DijkstraShortestPath pathMaker) {
        this.graph = graph;
        this.pathMaker = pathMaker;
    }

    public static PathFinder of(PathGraphCreator provider) {
        PathGraph pathGraph = provider.createPathGraph();
        return new PathFinder(pathGraph.getGraph(), new DijkstraShortestPath(pathGraph.getGraph()));
    }

    public Path findPath(PathVertex source, PathVertex target) {
        validateContainSourceVertexInGraph(source);
        validateContainTargetVertexInGraph(target);
        validateConnectedVertexes(source, target);
        return new Path(this.pathMaker.getPath(source, target));
    }

    public boolean isConnectedPath(PathVertex source, PathVertex target) {
        return this.graph.containsVertex(source) && this.graph.containsVertex(target)
                && Double.isFinite(this.pathMaker.getPathWeight(source, target));
    }

    private void validateConnectedVertexes(PathVertex source, PathVertex target) {
        if (Double.isInfinite(this.pathMaker.getPathWeight(source, target))) {
            throw new NoConnectedVertexesException();
        }
    }

    private void validateContainTargetVertexInGraph(PathVertex target) {
        if (!this.graph.containsVertex(target)) {
            throw new NotContainVertexException("도착점이 경로에 포함되어 있지 않습니다.");
        }
    }

    private void validateContainSourceVertexInGraph(PathVertex source) {
        if (!this.graph.containsVertex(source)) {
            throw new NotContainVertexException("시작점이 경로에 포함되어 있지 않습니다.");
        }
    }
}
