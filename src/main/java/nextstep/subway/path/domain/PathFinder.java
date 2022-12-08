package nextstep.subway.path.domain;

import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.domain.Stations;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder(Stations stations, Sections sections) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        stations.getList().forEach(graph::addVertex);
        sections.getList().forEach(this::addEdgeWeight);
    }

    private void addEdgeWeight(Section section) {
        graph.setEdgeWeight(
            graph.addEdge(section.getUpStation(), section.getDownStation()),
            section.getDistanceValue()
        );
    }

    public Path findPath(Station source, Station target) {
        validateSameSourceAndTarget(source, target);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);
        validateNotReachable(graphPath);

        Stations stations = Stations.from(graphPath.getVertexList());
        Distance distance = Distance.from(graphPath.getWeight());

        return Path.of(stations, distance);
    }

    private void validateNotReachable(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        if (Objects.isNull(graphPath)) {
            throw new IllegalArgumentException("출발역과 도착역이 연결되어있지 않습니다.");
        }
    }

    private void validateSameSourceAndTarget(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을수는 없습니다.");
        }
    }
}
