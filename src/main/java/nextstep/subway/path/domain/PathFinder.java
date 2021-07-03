package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private DijkstraShortestPath<Station, SectionEdge> shortestPathOfStations;
    private WeightedMultigraph<Station, SectionEdge> graph;

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
        this.graph = new WeightedMultigraph<>(SectionEdge.class);
    }

    private void createDijkstraShortestPath() {
        this.shortestPathOfStations = new DijkstraShortestPath<>(this.graph);
    }

    private void addVertex(final List<Line> lines) {
        lines.forEach(line -> line.getStations().forEach(graph::addVertex));
    }

    private void addEdgeWeight(final List<Line> lines) {
        lines.forEach(line -> line.getSections().forEach(this::setEdgeWeightToGraph));
    }

    private void setEdgeWeightToGraph(final Section section) {
        this.graph.setEdgeWeight(addEdge(section), section.getDistance());
    }

    private SectionEdge addEdge(final Section section) {
        SectionEdge sectionEdge = new SectionEdge(section);
        graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        return sectionEdge;
    }
}
