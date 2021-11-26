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
        getStations(sections).forEach(it -> this.addVertex(it));
    }

    public void addAllEdgeAndEdgeWeight(List<Section> sections) {
        sections.forEach(it ->
                addEdgeAndEdgeWeight(SectionEdge.of(it.getUpStation(), it.getDownStation(), it.getIntegerDistance())));
    }

    public void addEdgeAndEdgeWeight(SectionEdge sectionEdge) {
        this.setEdgeWeight(this.addEdge(sectionEdge.getSource(), sectionEdge.getTarget()), sectionEdge.getWeight());
    }

    public List<Station> getStations(List<Section> sections) {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }
        Set<Station> result = new LinkedHashSet<>();
        sections.forEach(f -> {
            result.add(f.getUpStation());
            result.add(f.getDownStation());
        });
        return new ArrayList<>(result);
    }

}
