package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    private final WeightedMultigraph<Long, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
    public static PathFinder of(Lines lines) {
        return new PathFinder(lines);
    }

    private PathFinder(Lines lines) {
        lines.makeGraph(graph);
    }

    public Path findShortestPath(Station source, Station target) {
        validatePathFinder(source, target);
        try {
            DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
            GraphPath<Long, SectionEdge> graphPath = dijkstraShortestPath.getPath(source.getId(), target.getId());
            int distance = (int) dijkstraShortestPath.getPath(source.getId(), target.getId()).getWeight();
            return new Path(graphPath.getVertexList(), distance, graphPath.getEdgeList());
        }
        catch (RuntimeException e) {
            throw new RuntimeException("경로 탐색에 실패하였습니다.");
        }
    }

    private void validatePathFinder(Station source, Station target) {
        if (source.getId().equals(target.getId())) {
            throw new RuntimeException("동일한 두 구간에 대한 탐색은 할 수 없습니다.");
        }
    }
}
