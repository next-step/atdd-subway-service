package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Collection;
import java.util.List;

public class PathFinder {

    public Path findShortestPath(List<Line> lines, Station source, Station target) {
        validateSameStation(source, target);
        try {
            GraphPath path = createShortestPath(lines).getPath(source, target);
            checkNotNull(path);
            return new Path(path.getVertexList(), (int) path.getWeight(), path.getEdgeList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("출발역과 도착역이 노선에 포함되어 있지 않습니다.");
        }
    }

    private void checkNotNull(GraphPath path) {
        if (path == null) {
            throw new IllegalStateException("출발역과 도착역이 연결이 되어 있지 않습니다.");
        }
    }

    private void validateSameStation(Station source, Station target) {
        if (source == target) {
            throw new IllegalArgumentException("출발지와 목적지가 같을 수 없습니다.");
        }
    }

    private DijkstraShortestPath createShortestPath(List<Line> lines) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph(SectionEdge.class);
        addStation(graph, lines);
        addDistance(graph, lines);
        return new DijkstraShortestPath(graph);
    }

    private void addStation(WeightedMultigraph<Station, SectionEdge> graph, List<Line> lines) {
        lines.stream()
            .map(line -> line.getStations())
            .flatMap(Collection::stream)
            .distinct()
            .forEach(graph::addVertex);
    }

    private void addDistance(WeightedMultigraph<Station, SectionEdge> graph, List<Line> lines) {
        lines.stream()
            .map(line -> line.getSections().getSections())
            .flatMap(Collection::stream)
            .forEach(section -> {
                SectionEdge edge = new SectionEdge(section);
                graph.addEdge((Station) edge.getSource(), (Station) edge.getTarget(), edge);
                graph.setEdgeWeight(edge, edge.getWeight());
            });
    }
}
