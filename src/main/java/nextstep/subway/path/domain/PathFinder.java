package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private DijkstraShortestPath<Station, SectionEdge> algorithm;
    private WeightedGraph<Station, SectionEdge> graph;

    public PathFinder(List<Line> lines) {
        graph = generateGraph(lines);
        this.algorithm = new DijkstraShortestPath<>(graph);
    }

    private WeightedGraph<Station, SectionEdge> generateGraph(List<Line> lines) {
        WeightedGraph<Station, SectionEdge> newGraph = new WeightedMultigraph<>(SectionEdge.class);

        addVertex(lines, newGraph);
        addEdge(lines, newGraph);

        return newGraph;

    }

    /* 리팩토링 필요 */
    private void addEdge(List<Line> lines, WeightedGraph<Station, SectionEdge> graph) {
        lines.forEach(line -> line.getSections().getSections()
                .forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().getDistanceValue())));

    }

    private void addVertex(List<Line> lines, WeightedGraph<Station, SectionEdge> graph) {
        lines.forEach(line -> line.getSortedStation()
                .forEach(graph::addVertex));
    }

    public Path getDijkstraShortestPath(Station startStation, Station endStation) {
        List<Station> path = algorithm.getPath(startStation, endStation).getVertexList();
        int totalDistance = (int) algorithm.getPathWeight(startStation, endStation);
        return new Path(path, totalDistance);
    }
}
