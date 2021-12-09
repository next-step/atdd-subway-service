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

public class PathFinder {
    private static final String SOURCE_TARGET_SAME_MESSAGE = "출발역과 도착역이 같은 경우 경로 조회 할 수 없습니다.";
    private static final String SOURCE_TARGET_NOT_LINK_MESSAGE = "출발역과 도착역이 연결이 되어 있지 않은 경우 경로 조회 할 수 없습니다.";
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        addVertexes(lines, graph);
        setEdgeWeights(lines, graph);
    }

    public static PathFinder ofList(List<Line> lines) {
        return new PathFinder(lines);
    }

    public Path findShortestPath(Station source, Station target) {
        validate(source, target);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath(source, target);
        isNull(path);
        return Path.of(path.getVertexList(), path.getWeight());
    }

    private void isNull(GraphPath path) {
        if(path == null) {
            throw new PathException(SOURCE_TARGET_NOT_LINK_MESSAGE);
        }
    }

    private void validate(Station source, Station target) {
        if(isSameStation(source, target)) {
            throw new PathException(SOURCE_TARGET_SAME_MESSAGE);
        }
    }

    private boolean isSameStation(Station source, Station target) {
        return source.equals(target);
    }

    private void setEdgeWeights(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        lines.stream()
                .map(line -> line.getSections())
                .map(sections -> sections.getSections())
                .flatMap(List::stream)
                .collect(Collectors.toList())
                .forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().getValue()));
    }

    private void addVertexes(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        lines.stream()
                .map(line -> line.getSections())
                .map(sections -> sections.getStations())
                .flatMap(List::stream)
                .forEach(graph::addVertex);
    }
}
