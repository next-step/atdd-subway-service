package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class StationGraph {

    private final WeightedMultigraph<Station, Section> graph = new WeightedMultigraph<>(Section.class);

    public StationGraph(List<Section> sections) {
        sections.forEach(this::addVertexAndEdgeWeight);
    }

    public WeightedMultigraph<Station, Section> getGraph() {
        return graph;
    }

    private void addVertexAndEdgeWeight(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        graph.addVertex(upStation);
        graph.addVertex(downStation);
        graph.addEdge(upStation, downStation, section);
        graph.setEdgeWeight(graph.getEdge(upStation, downStation), section.getDistance().getValue());
    }

    public boolean doesNotContain(Station station) {
        return !graph.containsVertex(station);
    }
}
