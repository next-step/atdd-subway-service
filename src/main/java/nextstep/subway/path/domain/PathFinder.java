package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.IncompleteLoginMember;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

public class PathFinder {
    public static final String START_STATION_IS_SAME_AS_END_STATION_EXCEPTION_MESSAGE = "출발역과 도착역이 같을 수 없습니다.";
    public static final String STATION_IS_NOT_CONNECTED_EXCEPTION_MESSAGE = "출발역과 도착역이 연결이 되어 있지 않습니다.";
    public static final String NOT_EXIST_STATION_EXCEPTION_MESSAGE = "존재하지 않은 출발역이나 도착역을 조회 할 수 없습니다.";

    private final WeightedMultigraph<Station, SectionEdge> graph;
    private final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        this.graph = generateGraph(lines);
        dijkstraShortestPath = new DijkstraShortestPath<>(this.graph);
    }

    private WeightedMultigraph<Station, SectionEdge> generateGraph(List<Line> lines) {
        WeightedMultigraph<Station, SectionEdge> weightedMultiGraph = new WeightedMultigraph<>(SectionEdge.class);
        lines.stream()
                .flatMap(line -> line.getUnmodifiableSectionList().stream())
                .forEach(section -> {
                    addVertex(weightedMultiGraph, section);
                    addEdge(weightedMultiGraph, section);
                });
        return weightedMultiGraph;
    }

    private void addEdge(WeightedMultigraph<Station, SectionEdge> weightedMultiGraph, Section section) {
        SectionEdge sectionEdge = SectionEdge.of(section);
        weightedMultiGraph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        weightedMultiGraph.setEdgeWeight(sectionEdge, section.getDistance().getValue());
    }

    private void addVertex(WeightedMultigraph<Station, SectionEdge> weightedMultiGraph, Section section) {
        weightedMultiGraph.addVertex(section.getUpStation());
        weightedMultiGraph.addVertex(section.getDownStation());
    }

    public SubwayShortestPath findPath(Station startStation, Station endStation, IncompleteLoginMember incompleteLoginMember) {
        validateStation(startStation, endStation);
        GraphPath<Station, SectionEdge> path = dijkstraShortestPath.getPath(startStation, endStation);
        validatePathIsNull(path);
        return new SubwayShortestPath(path.getVertexList(), (int) path.getWeight(), FareCalculator.newInstance().calculateFare(path, incompleteLoginMember));
    }

    private void validatePathIsNull(GraphPath<Station, SectionEdge> path) {
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
