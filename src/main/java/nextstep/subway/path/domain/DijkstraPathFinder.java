package nextstep.subway.path.domain;

import java.util.*;
import java.util.stream.*;

import org.jgrapht.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;

import nextstep.subway.line.domain.*;
import nextstep.subway.station.domain.*;

public class DijkstraPathFinder implements PathFinder {
    private final DijkstraShortestPath<Station, WeightedEdgeWithLine> dijkstraShortestPath;

    private DijkstraPathFinder(List<Line> lines) {
        WeightedMultigraph<Station, WeightedEdgeWithLine> graph = new WeightedMultigraph<>(WeightedEdgeWithLine.class);
        lines.forEach(line -> line.addVertexTo(graph));
        lines.forEach(line -> line.setEdgeWeight(graph));

        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public static DijkstraPathFinder from(List<Line> lines) {
        return new DijkstraPathFinder(lines);
    }

    public Path shortestPath(Station source, Station target) {
        GraphPath<Station, WeightedEdgeWithLine> graphPath = dijkstraShortestPath.getPath(source, target);
        return Path.of(graphPath.getVertexList(), (int)graphPath.getWeight(), flatLines(graphPath));
    }

    private List<Line> flatLines(GraphPath<Station, WeightedEdgeWithLine> shortestPath) {
        return shortestPath.getEdgeList()
            .stream()
            .map(WeightedEdgeWithLine::line)
            .collect(Collectors.toList());
    }
}
