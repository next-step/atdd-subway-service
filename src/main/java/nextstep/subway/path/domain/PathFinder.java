package nextstep.subway.path.domain;

import nextstep.subway.line.consts.ErrorMessage;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        registerLines(graph, lines);
        this.dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    public static PathFinder from(List<Line> lines) {
        return new PathFinder(lines);
    }

    private void registerLines(WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph, List<Line> lines) {
        for (Line line : lines) {
            addVertexes(routeGraph, line);
            addEdges(routeGraph, line);
        }
    }

    private void addVertexes(WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph, Line line) {
        line.getStations().stream()
                .forEach(station -> routeGraph.addVertex(station));
    }

    private void addEdges(WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph, Line line) {
        line.getSections().stream()
                .forEach(section -> routeGraph.setEdgeWeight(
                        addEdge(routeGraph, section), section.getDistance().value())
                );
    }

    private DefaultWeightedEdge addEdge(WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph, Section section) {
        return routeGraph.addEdge(section.getUpStation(), section.getDownStation());
    }

    public Path findShortestPath(Station sourceStation, Station targetStation) {
        validateInputStations(sourceStation, targetStation);
        GraphPath<Station, DefaultWeightedEdge> resultPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        validateResult(resultPath);
        return Path.of(resultPath.getVertexList(), (int) resultPath.getWeight());
    }

    private void validateResult(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException(ErrorMessage.ERROR_PATH_NOT_FOUND);
        }
    }

    private void validateInputStations(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(ErrorMessage.ERROR_PATH_SAME_SOURCE_TARGET);
        }
    }
}
