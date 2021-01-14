package nextstep.subway.path.domain;

import nextstep.subway.common.exception.CustomException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> path;
    private final List<Line> lines;
    private GraphPath<Station, DefaultWeightedEdge> resultPath;

    public PathFinder(List<Line> lines) {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.path = new DijkstraShortestPath<>(graph);
        this.lines = lines;
        createStationGraph();
    }

    public void findShortestPath(Station source, Station target) {
        validateStation(source, target);
        this.resultPath = path.getPath(source, target);
    }

    public List<Station> getStationsInShortestPath() {
        return resultPath.getVertexList();
    }

    public int getDistanceInShortestPath() {
        return (int) resultPath.getWeight();
    }

    private void createStationGraph() {
        createVertexInLines();
        createEdgeWeightInLines();
    }

    private void createVertexInLines() {
        List<Station> stationsInLines = getStationsInLines();
        stationsInLines.forEach(graph::addVertex);
    }

    private List<Station> getStationsInLines() {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toList());
    }

    private void createEdgeWeightInLines() {
        lines.forEach(this::addEdgeWeightInLine);
    }

    private void addEdgeWeightInLine(Line line) {
        line.getSections()
                .forEach(section -> graph.setEdgeWeight(createEdge(section), section.getDistance()));
    }

    private DefaultWeightedEdge createEdge(nextstep.subway.line.domain.Section section) {
        return graph.addEdge(section.getUpStation(), section.getDownStation());
    }

    private void validateStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new CustomException("출발역과 도착역이 동일합니다.");
        }
        if (isInvalidPath(source, target)) {
            throw new CustomException("출발역과 도착역이 연결이 되어 있지 않습니다.");
        }
    }

    private boolean isInvalidPath(Station source, Station target) {
        try {
            if (path.getPath(source, target) == null) {
                return true;
            }
            return false;
        } catch (IllegalArgumentException ex) {
            return true;
        }
    }
}
