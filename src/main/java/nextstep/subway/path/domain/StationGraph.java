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

    private final WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph(
            SectionEdge.class);

    public StationGraph(List<Section> sections) {
        sections.forEach(this::addGraphEdge);
    }

    public void addGraphEdge(Section section) {
        SectionEdge sectionEdge = new SectionEdge(section);
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
        graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        graph.setEdgeWeight(sectionEdge, section.getDistance().value());
    }

    public WeightedMultigraph<Station, SectionEdge> getGraph() {
        return graph;
    }

}
