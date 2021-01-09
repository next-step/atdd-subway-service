package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathSelector {
    private static WeightedMultigraph<Station, DefaultWeightedEdge> graph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private static DijkstraShortestPath<Station, DefaultWeightedEdge> path
            = new DijkstraShortestPath<>(graph);

    public static void add(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        graph.addVertex(upStation);
        graph.addVertex(downStation);
        graph.setEdgeWeight(graph.addEdge(upStation,downStation), section.getDistance().value());
    }

    public static void remove(Section section) {
        graph.removeEdge(section.getUpStation(), section.getDownStation());
    }

    public static PathResult select(Station source, Station target) {
        return new PathResult(path.getPath(source, target));
    }

    public static void clear() {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        path = new DijkstraShortestPath<>(graph);
    }
}