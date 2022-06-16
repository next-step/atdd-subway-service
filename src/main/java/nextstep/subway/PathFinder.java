package nextstep.subway;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    private final DijkstraShortestPath dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        lines.forEach(line ->
                line.getStationsInOrder().forEach(station -> graph.addVertex(station)));
        lines.forEach(line ->
                line.getSections().forEach(section ->
                        graph.setEdgeWeight(
                                graph.addEdge(section.getUpStation(), section.getDownStation()),
                                section.getDistance())));

        dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    public List<Station> findRoute(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }
}
