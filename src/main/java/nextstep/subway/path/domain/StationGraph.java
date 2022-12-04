package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import nextstep.subway.ErrorMessage;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class StationGraph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(
            DefaultWeightedEdge.class);

    public StationGraph(List<Section> sections) {
        sections.forEach(this::addGraphEdge);
    }

    public void addGraphEdge(Section section) {
        addVertex(section.getUpStation());
        addVertex(section.getDownStation());
        addEdge(section);
    }

    public WeightedMultigraph<Station, DefaultWeightedEdge> getGraph() {
        return graph;
    }

    private void addEdge(Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance().value());
    }

    private void addVertex(Station station) {
        graph.addVertex(station);
    }


    public boolean containsStation(Station station) {
        return graph.containsVertex(station);
    }
}
