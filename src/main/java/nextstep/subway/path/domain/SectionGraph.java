package nextstep.subway.path.domain;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

public class SectionGraph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
        DefaultWeightedEdge.class);

    public SectionGraph(final Sections sections) {
        addVertex(sections);
        addEdge(sections);
    }

    private void addVertex(final Sections sections) {
        sections.mergeStations()
            .forEach(graph::addVertex);
    }

    private void addEdge(final Sections sections) {
        sections.toList()
            .forEach(
                it -> {
                    final DefaultWeightedEdge edge = graph.addEdge(it.getUpStation(), it.getDownStation());
                    graph.setEdgeWeight(edge, it.getDistance());
                });
    }

    public Graph<Station, DefaultWeightedEdge> graph() {
        return graph;
    }
}
