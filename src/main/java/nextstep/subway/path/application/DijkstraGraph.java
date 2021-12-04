package nextstep.subway.path.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class DijkstraGraph implements Graph {

    private final WeightedMultigraph<Station, SectionEdge> graph;

    public DijkstraGraph() {
        graph = new WeightedMultigraph(SectionEdge.class);
    }

    @Override
    public Path getPath(Sections sections) {
        sections.getAllStations().forEach(graph::addVertex);
        sections.getSections().forEach(this::addEdgeWeight);
        return new DijkstraPath(graph);
    }

    private void addEdgeWeight(Section section) {
        SectionEdge sectionEdge = graph.addEdge(section.getUpStation(), section.getDownStation());
        sectionEdge.addSection(section);

        graph.setEdgeWeight(sectionEdge, section.getDistance());
    }
}
