package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Graph {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public Graph() {
        this.graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    }

    public void addVertex(Station station) {
        graph.addVertex(station);
    }

    public void setEdgeWeight(Station source, Station target, int distance) {
        graph.setEdgeWeight(graph.addEdge(source, target), distance);
    }

    public WeightedMultigraph<Station, DefaultWeightedEdge> getGraph() {
        return graph;
    }
}
