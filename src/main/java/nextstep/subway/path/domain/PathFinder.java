package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

public class PathFinder {
    public static final String START_STATION_IS_SAME_AS_END_STATION_EXCEPTION_MESSAGE = "출발역과 도착역이 같을 수 없습니다.";
    public static final String STATION_IS_NOT_CONNECTED_EXCEPTION_MESSAGE = "출발역과 도착역이 연결이 되어 있지 않습니다.";
    public static final String NOT_EXIST_STATION_EXCEPTION_MESSAGE = "존재하지 않은 출발역이나 도착역을 조회 할 수 없습니다.";

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        this.graph = generateGraph(lines);
        dijkstraShortestPath = new DijkstraShortestPath<>(this.graph);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> generateGraph(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> weightedMultiGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        lines.stream()
                .flatMap(line -> line.getUnmodifiableSectionList().stream())
                .forEach(section -> {
                    addVertex(weightedMultiGraph, section);
                    addEdge(weightedMultiGraph, section);
                });
        return weightedMultiGraph;
    }

    private void addEdge(WeightedMultigraph<Station, DefaultWeightedEdge> weightedMultiGraph, Section section) {
        DefaultWeightedEdge defaultWeightedEdge = weightedMultiGraph.addEdge(section.getUpStation(), section.getDownStation());
        weightedMultiGraph.setEdgeWeight(defaultWeightedEdge, section.getDistance().getValue());
    }

    private void addVertex(WeightedMultigraph<Station, DefaultWeightedEdge> weightedMultiGraph, Section section) {
        weightedMultiGraph.addVertex(section.getUpStation());
        weightedMultiGraph.addVertex(section.getDownStation());
    }

    public SubwayShortestPath findPath(Station startStation, Station endStation) {
        validateStation(startStation, endStation);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(startStation, endStation);
        validatePathIsNull(path);
        int distance = (int) path.getWeight();
        int fare = DistanceFare.findDistanceFareByDistance(distance).calculateFare(distance);
        return new SubwayShortestPath(path.getVertexList(), distance, fare);
    }

    private void validatePathIsNull(GraphPath<Station, DefaultWeightedEdge> path) {
        if (Objects.isNull(path)) {
            throw new IllegalArgumentException(STATION_IS_NOT_CONNECTED_EXCEPTION_MESSAGE);
        }
    }

    private void validateStation(Station startStation, Station endStation) {
        if (startStation.equals(endStation)) {
            throw new IllegalArgumentException(START_STATION_IS_SAME_AS_END_STATION_EXCEPTION_MESSAGE);
        }
        if (!graph.containsVertex(startStation) || !graph.containsVertex(endStation)) {
            throw new IllegalArgumentException(NOT_EXIST_STATION_EXCEPTION_MESSAGE);
        }
    }
}
