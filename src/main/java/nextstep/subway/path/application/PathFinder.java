package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Collection;
import java.util.List;

public class PathFinder {

    private final DijkstraShortestPath shortestPath;

    public PathFinder(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        addStation(graph, lines);
        addDistance(graph, lines);
        shortestPath = new DijkstraShortestPath(graph);
    }

    private void addStation(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Line> lines) {
        lines.stream()
            .map(line -> line.getStations())
            .flatMap(Collection::stream)
            .distinct()
            .forEach(graph::addVertex);
    }

    private void addDistance(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Line> lines) {
        lines.stream()
            .map(line -> line.getSections().getSections())
            .flatMap(Collection::stream)
            .forEach(section ->
                graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().getDistance())
            );
    }

    public Path findShortestPath(Station source, Station target) {
        validateSameStation(source, target);
        GraphPath path = shortestPath.getPath(source, target);
        return new Path(path.getVertexList(), (int) path.getWeight());
    }

    private void validateSameStation(Station source, Station target) {
        if (source == target) {
            throw new IllegalArgumentException("출발지와 목적지가 같을 수 없습니다.");
        }
    }
}
