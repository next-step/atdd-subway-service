package nextstep.subway.path.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraGraph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public DijkstraGraph() {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    }

    public DijkstraPath getPath(Sections sections) {
        sections.getStations().forEach(graph::addVertex);
        sections.getSections().forEach(this::addEdgeWeight);
        return new DijkstraPath(graph);
    }

    private void addEdgeWeight(Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }
}
