package nextstep.subway.path.application;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.message.LinePathMessage;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class LinePathGraph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> delegate;

    public LinePathGraph(List<Line> lines) {
        this.delegate = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        lines.forEach(line -> line.addPathGraph(this));
    }

    public LinePath getShortestPath(Station source, Station target) {
        validateStations(source, target);

        DijkstraShortestPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(delegate);
        GraphPath<Station, DefaultWeightedEdge> graphPath = path.getPath(source, target);

        validateGraphPath(graphPath);
        return LinePath.of(graphPath.getVertexList(), (int) graphPath.getWeight());
    }

    public void addGraphVertex(Station station) {
        if(isContainsVertex(station)) {
            return;
        }
        delegate.addVertex(station);
    }

    public void addEdge(Station source, Station target, Distance distance) {
        validateNewEdge(source, target);

        DefaultWeightedEdge edge = delegate.addEdge(source, target);
        delegate.setEdgeWeight(edge, distance.value());
    }

    private void validateNewEdge(Station source, Station target) {
        validateStations(source, target);

        if(isContainsEdge(source, target)) {
            throw new IllegalArgumentException(LinePathMessage.GRAPH_ERROR_IS_ALREADY_ENROLLED_EDGE.message());
        }
    }

    private void validateStations(Station source, Station target) {
        if(source.equals(target)) {
            throw new IllegalArgumentException(LinePathMessage.GRAPH_ERROR_SOURCE_AND_TARGET_STATION_IS_EQUALS.message());
        }

        if(!isContainsVertex(source)) {
            throw new IllegalArgumentException(LinePathMessage.GRAPH_ERROR_NOT_FOUND_SOURCE_STATION.message());
        }

        if(!isContainsVertex(target)) {
            throw new IllegalArgumentException(LinePathMessage.GRAPH_ERROR_NOT_FOUND_TARGET_STATION.message());
        }
    }

    private boolean isContainsEdge(Station source, Station target) {
        return delegate.containsEdge(source, target);
    }


    private boolean isContainsVertex(Station source) {
        return delegate.containsVertex(source);
    }

    private void validateGraphPath(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        if(graphPath == null) {
            throw new IllegalArgumentException(LinePathMessage.GRAPH_ERROR_NOT_CONNECTED_STATIONS.message());
        }
    }
}
