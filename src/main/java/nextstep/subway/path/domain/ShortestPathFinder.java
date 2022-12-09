package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.message.PathMessage;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class ShortestPathFinder implements PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

    public ShortestPathFinder(List<Line> lines) {
        graph = makeGraph(lines);
        shortestPath = new DijkstraShortestPath<>(graph);
    }

    @Override
    public Path findPath(Station source, Station target) {
        validateStations(source, target);

        GraphPath<Station, DefaultWeightedEdge> graphPath = shortestPath.getPath(source, target);
        validateGraphPath(graphPath);
        return Path.of(graphPath.getVertexList(), (int) graphPath.getWeight());
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> makeGraph(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        lines.forEach( line -> {
            this.addGraphVertex(graph, line);
            this.addGraphEdge(graph, line);
        });
        return graph;
    }

    private void addGraphVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        line.getStations().forEach(graph::addVertex);
    }

    private void addGraphEdge(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        line.getSections().forEach(section -> {
            DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
            Distance distance = section.getDistance();
            graph.setEdgeWeight(edge, distance.value());
        });
    }

    private void validateStations(Station source, Station target) {
        if(source.equals(target)) {
            throw new IllegalArgumentException(PathMessage.GRAPH_ERROR_SOURCE_AND_TARGET_STATION_IS_EQUALS.message());
        }

        if(!graph.containsVertex(source)) {
            throw new IllegalArgumentException(PathMessage.GRAPH_ERROR_NOT_FOUND_SOURCE_STATION.message());
        }

        if(!graph.containsVertex(target)) {
            throw new IllegalArgumentException(PathMessage.GRAPH_ERROR_NOT_FOUND_TARGET_STATION.message());
        }
    }

    private void validateGraphPath(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        if(graphPath == null) {
            throw new IllegalArgumentException(PathMessage.GRAPH_ERROR_NOT_CONNECTED_STATIONS.message());
        }
    }
}
