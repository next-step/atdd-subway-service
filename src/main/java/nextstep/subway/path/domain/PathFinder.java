package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.path.exception.InvalidPathStationException;
import nextstep.subway.path.exception.PathNotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private PathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static PathFinder of(final List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = buildGraph(lines);
        return new PathFinder(graph);
    }

    public PathResponse findShortestPath(final Station sourceStation, final Station targetStation) {
        validateDifferentStation(sourceStation, targetStation);
        validateContainsStation(sourceStation, targetStation);

        return PathResponse.of(
            Path.of(DijkstraShortestPath.findPathBetween(graph, sourceStation, targetStation))
        );
    }

    private void validateContainsStation(Station sourceStation, Station targetStation) {
        if (!graph.containsVertex(sourceStation) || !graph.containsVertex(targetStation)) {
            throw new PathNotFoundException("역 사이의 경로를 찾을 수 없습니다.");
        }
    }

    private void validateDifferentStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new InvalidPathStationException("출발역과 도착역이 동일합니다.");
        }
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> buildGraph(final List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
            DefaultWeightedEdge.class);

        buildVertex(graph, lines);
        buildEdge(graph, lines);

        return graph;
    }

    private static void buildVertex(final WeightedMultigraph<Station, DefaultWeightedEdge> graph,
        final List<Line> lines) {
        lines.stream()
            .map(Line::getStations)
            .flatMap(List::stream)
            .forEach(graph::addVertex);
    }

    private static void buildEdge(final WeightedMultigraph<Station, DefaultWeightedEdge> graph,
        final List<Line> lines) {
        lines.forEach(line -> line.getSections().getSections()
            .forEach(section -> graph.setEdgeWeight(
                graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance().getDistance())
            ));
    }
}
