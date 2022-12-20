package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.constants.ErrorMessages;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    public static Stations findPath(List<Line> lines, Station source, Station target) {
        checkSourceTargetNotMatch(source, target);
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = createStationGraph(lines);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        checkPathExist(path);

        return getStationsFromPath(path);
    }

    private static void checkSourceTargetNotMatch(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(ErrorMessages.SOURCE_TARGET_CANNOT_BE_SAME);
        }
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> createStationGraph(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertexes(graph, lines);
        addEdges(graph, lines);
        return graph;
    }

    private static void addVertexes(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Line> lines) {
        lines.stream()
                .map(Line::getStations)
                .flatMap(Stations::stream)
                .forEach(graph::addVertex);
    }

    private static void addEdges(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Line> lines) {
        lines.stream()
                .map(Line::getSections)
                .flatMap(Sections::stream)
                .forEach(section -> graph.setEdgeWeight(
                        graph.addEdge(section.getUpStation(), section.getDownStation())
                        , section.getDistanceValue()));
    }

    private static void checkPathExist(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new RuntimeException(ErrorMessages.CANNOT_FIND_ANY_PATH);
        }
    }

    private static Stations getStationsFromPath(GraphPath<Station, DefaultWeightedEdge> path) {
        Stations stations = new Stations();
        stations.addAll(path.getVertexList());
        return stations;
    }
}
