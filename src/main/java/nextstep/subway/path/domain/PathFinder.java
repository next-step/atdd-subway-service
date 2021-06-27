package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPathOfStations;
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public static PathFinder of(final List<Line> lines) {
        PathFinder pathFinder = new PathFinder();
        pathFinder.createGraph();
        pathFinder.addVertex(lines);
        pathFinder.addEdgeWeight(lines);
        pathFinder.createDijkstraShortestPath();
        return pathFinder;
    }

    public Paths getShortestPaths(final Station sourceStation, final Station targetStation) {
        return Paths.of(shortestPathOfStations.getPath(sourceStation, targetStation));
    }

    private void createGraph() {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    private void createDijkstraShortestPath() {
        this.shortestPathOfStations = new DijkstraShortestPath<>(this.graph);
    }

    private void addVertex(final List<Line> lines) {
        lines.stream()
             .flatMap(line -> line.getStations().stream())
             .forEach(graph::addVertex);
    }

    private void addEdgeWeight(final List<Line> lines) {
        lines.stream()
             .flatMap(line -> line.getSections().stream())
             .forEach(section -> graph.setEdgeWeight(addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()));
    }

    private DefaultWeightedEdge addEdge(final Station upStation, final Station downStation) {
        return graph.addEdge(upStation, downStation);
    }
}
