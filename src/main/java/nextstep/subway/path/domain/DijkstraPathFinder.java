package nextstep.subway.path.domain;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;

import nextstep.subway.fare.*;
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

        int fareByDistance = FareByDistancePolicy.calculateFare(graphPath);
        int extraFareByLine = calculateExtraFareByLine(graphPath);
        return Path.of(graphPath.getVertexList(), (int)graphPath.getWeight(), fareByDistance + extraFareByLine);
    }

    private int calculateExtraFareByLine(GraphPath<Station, WeightedEdgeWithLine> shortestPath) {
        return shortestPath.getEdgeList()
            .stream()
            .map(WeightedEdgeWithLine::line)
            .mapToInt(Line::getExtraFare)
            .max()
            .orElse(0);
    }
}
