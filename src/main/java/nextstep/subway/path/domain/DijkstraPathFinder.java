package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.exception.PathException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class DijkstraPathFinder implements PathFinder {
    private static final String SOURCE_TARGET_SAME_MESSAGE = "출발역과 도착역이 같은 경우 경로 조회 할 수 없습니다.";
    private static final String SOURCE_TARGET_NOT_LINK_MESSAGE = "출발역과 도착역이 연결이 되어 있지 않은 경우 경로 조회 할 수 없습니다.";
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private DijkstraPathFinder(List<Line> lines) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        addVertexes(lines);
        setEdgeWeights(lines);
    }

    public static DijkstraPathFinder ofList(List<Line> lines) {
        return new DijkstraPathFinder(lines);
    }

    @Override
    public Path findPath(Station source, Station target) {
        validate(source, target);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath graphPath = dijkstraShortestPath.getPath(source, target);
        return getPath(graphPath);
    }

    private Path getPath(GraphPath graphPath) {
        try {
            return Path.of(graphPath.getVertexList(), graphPath.getWeight());
        } catch (NullPointerException e) {
            throw new PathException(SOURCE_TARGET_NOT_LINK_MESSAGE);
        }
    }

    private void validate(Station source, Station target) {
        if (isSameStation(source, target)) {
            throw new PathException(SOURCE_TARGET_SAME_MESSAGE);
        }
    }

    private boolean isSameStation(Station source, Station target) {
        return source.equals(target);
    }

    private void setEdgeWeights(List<Line> lines) {
        lines.stream()
                .map(line -> line.getSections())
                .map(sections -> sections.getSections())
                .flatMap(List::stream)
                .collect(Collectors.toList())
                .forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().getValue()));
    }

    private void addVertexes(List<Line> lines) {
        lines.stream()
                .map(line -> line.getSections())
                .map(sections -> sections.getStations())
                .flatMap(List::stream)
                .forEach(graph::addVertex);
    }
}
