package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraShortestPathFinder implements ShortestPathFinder {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public DijkstraShortestPathFinder(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = generateGraph(lines);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> generateGraph(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Line line : lines) {
            addVertexex(graph, line);
            addEdges(graph, line);
        }
        return graph;
    }

    private void addVertexex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        line.getStations()
                .forEach(graph::addVertex);
    }

    private void addEdges(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        line.getSections()
                .forEach(section -> graph.setEdgeWeight(
                        graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance())
                );
    }

    @Override
    public Path getPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);
        return Path.of(graphPath.getVertexList(), graphPath.getWeight());
    }
}
