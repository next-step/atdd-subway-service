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

public class PathFinder extends WeightedMultigraph<Station, DefaultWeightedEdge> {
    public static final String START_STATION_IS_SAME_AS_END_STATION_EXCEPTION_MESSAGE = "출발역과 도착역이 같을 수 없습니다.";
    public static final String STATION_IS_NOT_CONNECTED_EXCEPTION_MESSAGE = "출발역과 도착역이 연결이 되어 있지 않습니다.";
    public static final String NOT_EXIST_STATION_EXCEPTION_MESSAGE = "존재하지 않은 출발역이나 도착역을 조회 할 수 없습니다.";

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        super(DefaultWeightedEdge.class);
        initializeGraph(lines);
        dijkstraShortestPath = new DijkstraShortestPath<>(this);
    }

    private void initializeGraph(List<Line> lines){
        lines.stream()
                .flatMap(line -> line.getUnmodifiableSectionList().stream())
                .forEach(section -> {
                    addVertex(section);
                    addEdge(section);
                });
    }

    private void addEdge(Section section) {
        DefaultWeightedEdge defaultWeightedEdge = this.addEdge(section.getUpStation(), section.getDownStation());
        this.setEdgeWeight(defaultWeightedEdge, section.getDistance().getValue());
    }

    private void addVertex(Section section) {
        this.addVertex(section.getUpStation());
        this.addVertex(section.getDownStation());
    }

    public SubwayShortestPath findPath(Station startStation, Station endStation) {
        validateStation(startStation, endStation);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(startStation, endStation);
        validatePathIsNull(path);
        return new SubwayShortestPath(path.getVertexList(), (int) path.getWeight());
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
        if (!this.containsVertex(startStation) || !this.containsVertex(endStation)) {
            throw new IllegalArgumentException(NOT_EXIST_STATION_EXCEPTION_MESSAGE);
        }
    }
}
