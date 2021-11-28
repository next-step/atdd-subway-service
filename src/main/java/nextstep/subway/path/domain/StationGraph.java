package nextstep.subway.path.domain;

import nextstep.subway.line.domain.section.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.*;

public class StationGraph extends WeightedMultigraph<Station, SectionEdge> {

    public StationGraph() {
        super(SectionEdge.class);
    }

    public void addAllVertex(List<Section> sections) {
        getStations(sections).forEach(this::addVertex);
    }

    public void addAllEdgeAndEdgeWeight(List<Section> sections) {
        sections.stream()
                .map(SectionEdge::of)
                .forEach(this::addEdgeAndEdgeWeight);
    }

    public void addEdgeAndEdgeWeight(SectionEdge sectionEdge) {
        addEdge(sectionEdge.getSource(), sectionEdge.getTarget(), sectionEdge);
        setEdgeWeight(sectionEdge, sectionEdge.getWeight());
    }

    public List<Station> getStations(List<Section> sections) {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Station> result = new LinkedHashSet<>();
        sections.forEach(f -> {
            result.add(f.getUpStation());
            result.add(f.getDownStation());
        });
        return new ArrayList<>(result);
    }

}
