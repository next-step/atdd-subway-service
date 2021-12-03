package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathGraph {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathGraph() {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    public void addGraphVertex(final Stations stations) {
        stations.getStations()
                .forEach(graph::addVertex);
    }

    public DefaultWeightedEdge addSection(final Section section) {
        return graph.addEdge(section.getUpStation(), section.getDownStation());
    }

    public void setSectionDistance(final DefaultWeightedEdge edge, final int distance) {
        graph.setEdgeWeight(edge, distance);
    }

    public WeightedMultigraph<Station, DefaultWeightedEdge> getGraph() {
        return graph;
    }
}
