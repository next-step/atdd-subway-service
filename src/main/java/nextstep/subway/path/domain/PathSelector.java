package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathSelector {
    private final static WeightedMultigraph<Station, DefaultWeightedEdge> GRAPH
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final static DijkstraShortestPath<Station, DefaultWeightedEdge> PATH
            = new DijkstraShortestPath<>(GRAPH);

    public static void add(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        GRAPH.addVertex(upStation);
        GRAPH.addVertex(downStation);
        GRAPH.setEdgeWeight(GRAPH.addEdge(upStation,downStation), section.getDistance().value());
    }

    public static void remove(Section section) {
        GRAPH.removeEdge(section.getUpStation(), section.getDownStation());
    }

    public static PathResult select(Station source, Station target) {
        return new PathResult(PATH.getPath(source, target));
    }
}