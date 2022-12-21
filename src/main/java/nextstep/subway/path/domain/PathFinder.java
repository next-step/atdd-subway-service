package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class PathFinder {
    public static final String EQUAL_SOURCE_TARGET = "출발역과 도착역이 동일합니다.";
    public static final String STATION_NOT_FOUND = "존재하지 않는 역입니다.";
    public static final String NOT_CONNECTED_SOURCE_TARGET = "출발역과 도착역이 연결되어 있지 않습니다.";

    public PathFinderResponse findShortestPath(PathFinderRequest request) {
        validateNonEqualSourceTarget(request.getSource(), request.getTarget());

        return findShortestPath(createGraph(request.getLines()), request.getSource(), request.getTarget());
    }

    private void validateNonEqualSourceTarget(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation) || targetStation.equals(sourceStation)) {
            throw new IllegalPathException(EQUAL_SOURCE_TARGET);
        }
    }

    private static WeightedGraph<Station, DefaultWeightedEdge> createGraph(List<Line> lines) {
        WeightedGraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Station station : mapToStations(lines)) {
            graph.addVertex(station);
        }
        for (Section section : mapToSections(lines)) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().doubleValue());
        }
        return graph;
    }

    private static List<Section> mapToSections(List<Line> lines) {
        return lines.stream()
            .map(Line::getSections)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());
    }

    private static List<Station> mapToStations(List<Line> lines) {
        return lines.stream()
            .map(Line::getStations)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());
    }

    private static PathFinderResponse findShortestPath(
        WeightedGraph<Station, DefaultWeightedEdge> graph,
        Station sourceStation,
        Station targetStation
    ) {
        validateGraphContainsSourceAndTarget(graph, sourceStation, targetStation);

        GraphPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph).getPath(sourceStation, targetStation);

        validateNonNullPath(path);

        return new PathFinderResponse(path.getVertexList(), (int) path.getWeight());
    }

    private static void validateGraphContainsSourceAndTarget(
        WeightedGraph<Station, DefaultWeightedEdge> graph,
        Station source,
        Station target
    ) {
        if (!(graph.containsVertex(source) && graph.containsVertex(target))) {
            throw new IllegalPathException(STATION_NOT_FOUND);
        }
    }

    private static void validateNonNullPath(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalPathException(NOT_CONNECTED_SOURCE_TARGET);
        }
    }

}
