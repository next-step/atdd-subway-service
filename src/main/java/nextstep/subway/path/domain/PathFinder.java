package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.constants.ErrorMessages;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    public static GraphPath<Station, StationEdge> findPath(List<Line> lines, Station source, Station target) {
        checkSourceTargetNotMatch(source, target);
        WeightedMultigraph<Station, StationEdge> graph = createStationGraph(lines);
        DijkstraShortestPath<Station, StationEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, StationEdge> path = dijkstraShortestPath.getPath(source, target);
        checkPathExist(path);
        return path;
    }

    private static void checkSourceTargetNotMatch(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(ErrorMessages.SOURCE_TARGET_CANNOT_BE_SAME);
        }
    }

    private static WeightedMultigraph<Station, StationEdge> createStationGraph(List<Line> lines) {
        WeightedMultigraph<Station, StationEdge> graph
                = new WeightedMultigraph<>(StationEdge.class);
        addVertexes(graph, lines);
        addEdges(graph, lines);
        return graph;
    }

    private static void addVertexes(WeightedMultigraph<Station, StationEdge> graph, List<Line> lines) {
        lines.stream()
                .map(Line::getStations)
                .flatMap(Stations::stream)
                .forEach(graph::addVertex);
    }

    private static void addEdges(WeightedMultigraph<Station, StationEdge> graph, List<Line> lines) {
        lines.stream()
                .map(Line::getSections)
                .flatMap(Sections::stream)
                .forEach(section -> {
                    StationEdge edge = new StationEdge(section);
                    graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
                    graph.setEdgeWeight(edge, section.getDistanceValue());
                });
    }

    private static void checkPathExist(GraphPath<Station, StationEdge> path) {
        if (path == null) {
            throw new RuntimeException(ErrorMessages.CANNOT_FIND_ANY_PATH);
        }
    }
}
