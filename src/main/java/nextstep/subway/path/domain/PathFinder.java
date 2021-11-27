package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    private PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        lines.forEach(
                line -> line.getStations().forEach(
                        graph::addVertex));
        lines.forEach(
                line -> line.getSections().forEach(
                        section -> graph.setEdgeWeight(
                                    graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance())));

        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public static PathFinder from(List<Line> lines) {
        return new PathFinder(lines);
    }

    public Path getShortestPath(Station source, Station target) {
        return new Path(dijkstraShortestPath.getPath(source, target).getVertexList(),
                (int) dijkstraShortestPath.getPath(source, target).getWeight());
    }

}
