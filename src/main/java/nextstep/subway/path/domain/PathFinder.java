package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.ShortestPathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    public static final String SOURCE_AND_TARGET_IS_EQUAL_ERROR = "출발지와 도착지는 같을 수 없습니다.";
    public static final String SOURCE_OR_TARGET_IS_NOT_CONTAINS_ALL_LINE_STATION_ERROR = "대상 노선에서 해당역을 찾을 수 없습니다.";
    static WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    static {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    }

    public static ShortestPathResponse findShortestPath(List<Line> allLines, Station source, Station target) {
        validate(allLines, source, target);
        addSectionsToGraph(allLines);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath(source, target);
        checkResultIsNull(path);
        return ShortestPathResponse.from(path);
    }

    private static void validate(List<Line> allLines, Station source, Station target){
        validateStationEquals(source, target);
        validateStationContains(allLines, source, target);
    }

    private static void validateStationEquals(Station source, Station target) {
        if(source.equals(target)){
            throw new IllegalArgumentException(SOURCE_AND_TARGET_IS_EQUAL_ERROR);
        }
    }

    private static void validateStationContains(List<Line> allLines, Station source, Station target){
        List<Station> stations = mergeAllLinesStations(allLines);
        if(!stations.contains(source) || !stations.contains(target)){
            throw new IllegalArgumentException(SOURCE_OR_TARGET_IS_NOT_CONTAINS_ALL_LINE_STATION_ERROR);
        }
    }

    private static void addSectionsToGraph(List<Line> allLines) {
        addVertexByStation(mergeAllLinesStations(allLines));
        setEdgeWeightBySection(mergeAllLinesSections(allLines));
    }

    private static void addVertexByStation(List<Station> allLinesStations) {
        allLinesStations.forEach(it -> graph.addVertex(it));
    }

    private static void setEdgeWeightBySection(List<Section> allLinesSections) {
        allLinesSections.forEach(it ->
            graph.setEdgeWeight(
                graph.addEdge(it.getUpStation(), it.getDownStation()),
                it.getDistance()
            ));
    }

    private static List<Station> mergeAllLinesStations(List<Line> allLines) {
        return allLines.stream()
            .map(it -> it.getAllStations())
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private static List<Section> mergeAllLinesSections(List<Line> allLines) {
        return allLines.stream()
            .map(it -> it.getSections())
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private static void checkResultIsNull(GraphPath path) {
        if(path == null){
            throw new IllegalArgumentException("출발지와 도착지가 연결 되어있는지 확인하세요.");
        }
    }
}
