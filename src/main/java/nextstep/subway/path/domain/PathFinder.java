package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.ShortestPathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationSimpleResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private static final String SOURCE_AND_TARGET_IS_EQUAL_ERROR = "출발지와 도착지는 같을 수 없습니다.";
    private static final String SOURCE_OR_TARGET_IS_NOT_CONTAINS_ALL_LINE_STATION_ERROR = "대상 노선에서 해당역을 찾을 수 없습니다.";
    private static final String SOURCE_AND_TARGET_IS_NOT_CONNECTED_ERROR = "출발지와 도착지가 연결 되어있는지 확인하세요.";

    private WeightedMultigraph<Station, SectionWeightedEdge> graph;
    private DijkstraShortestPath<Station, SectionWeightedEdge> dijkstraShortestPath;
    private List<Line> allLines;

    private PathFinder(List<Line> allLines) {
        this.graph = new WeightedMultigraph<>(SectionWeightedEdge.class);
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        this.allLines = allLines;
        addSectionsToGraph();
    }

    public static PathFinder from(List<Line> allLines) {
        return new PathFinder(allLines);
    }

    public ShortestPathResponse findShortestPath(Station source, Station target) {
        validate(source, target);
        GraphPath<Station, SectionWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        checkResultIsNull(path);
        return ShortestPathResponse.of(path, toStationResponse(path.getVertexList()));
    }

    private List<StationSimpleResponse> toStationResponse(List<Station> vertexStationList) {
        return vertexStationList.stream()
            .map(StationSimpleResponse::from)
            .collect(Collectors.toList());
    }

    private void validate(Station source, Station target) {
        validateStationEquals(source, target);
        validateStationContains(source, target);
    }

    private void validateStationEquals(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(SOURCE_AND_TARGET_IS_EQUAL_ERROR);
        }
    }

    private void validateStationContains(Station source, Station target) {
        List<Station> stations = mergeAllLinesStations();
        if (!stations.contains(source) || !stations.contains(target)) {
            throw new IllegalArgumentException(SOURCE_OR_TARGET_IS_NOT_CONTAINS_ALL_LINE_STATION_ERROR);
        }
    }

    private void addSectionsToGraph() {
        addVertexByStation();
        setEdgeWeightBySection();
    }

    private void addVertexByStation() {
        List<Station> allLinesStations = mergeAllLinesStations();
        allLinesStations.forEach(graph::addVertex);
    }

    private void setEdgeWeightBySection() {
        List<Section> allLinesSections = mergeAllLinesSections();
        allLinesSections.forEach(it -> {
            SectionWeightedEdge sectionWeightedEdge = new SectionWeightedEdge(it);
            graph.addEdge(it.getUpStation(), it.getDownStation(), sectionWeightedEdge);
            graph.setEdgeWeight(sectionWeightedEdge, it.getDistance());
        });
    }

    private List<Station> mergeAllLinesStations() {
        return allLines.stream()
            .map(Line::getAllStations)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private List<Section> mergeAllLinesSections() {
        return allLines.stream()
            .map(Line::getSections)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private void checkResultIsNull(GraphPath<Station, SectionWeightedEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException(SOURCE_AND_TARGET_IS_NOT_CONNECTED_ERROR);
        }
    }
}
