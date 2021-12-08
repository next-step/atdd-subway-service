package nextstep.subway.path.domain.jgrapht;

import nextstep.subway.exception.NotFoundException;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.StationGraph;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class WeightedMultiStationGraph implements StationGraph {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public WeightedMultiStationGraph() {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    @Override
    public void addVertex(Station s) {
        graph.addVertex(s);
    }

    @Override
    public void addEdgeWithDistance(Station source, Station target, Integer distance) {
        graph.setEdgeWeight(graph.addEdge(source, target), distance);
    }

    @Override
    public boolean containsVertex(Station station) {
        return graph.containsVertex(station);
    }

    @Override
    public Path getShortestPath(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        if (path == null ) {
            throw new NotFoundException("점접이 없습니다.");
        }
        return Path.of(path.getVertexList(), Double.valueOf(path.getWeight()).intValue());
    }
}
