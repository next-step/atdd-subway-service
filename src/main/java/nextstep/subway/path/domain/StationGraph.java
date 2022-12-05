package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class StationGraph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
        DefaultWeightedEdge.class);

    public StationGraph(List<Section> sections) {
        sections.forEach(this::addVertexAndEdgeWeight);
    }

    public WeightedMultigraph<Station, DefaultWeightedEdge> getGraph() {
        return graph;
    }

    private void addVertexAndEdgeWeight(Section section) {
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }

    public boolean doesNotContain(Station station) {
        return !graph.containsVertex(station);
    }
}
