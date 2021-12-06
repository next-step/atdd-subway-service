package nextstep.subway.path.domain;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;

import nextstep.subway.line.domain.*;
import nextstep.subway.station.domain.*;

public class PathFinder {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    private PathFinder(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph;

        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        lines.forEach(line -> line.addVertexTo(graph));
        lines.forEach(line -> line.setEdgeWeight(graph));

        dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    public static PathFinder from(List<Line> lines) {
        return new PathFinder(lines);
    }

    public Path shortestPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);
        return Path.of(graphPath.getVertexList(), (int)graphPath.getWeight());
    }
}
