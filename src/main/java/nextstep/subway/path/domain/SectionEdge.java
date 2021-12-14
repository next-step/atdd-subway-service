package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.stream.Collectors;

public class SectionEdge extends DefaultWeightedEdge {

    private Section section;

    private SectionEdge() {
    }

    public static SectionEdge from(Section section) {
        SectionEdge sectionEdge = new SectionEdge();
        sectionEdge.section = section;
        return sectionEdge;
    }

    public static List<Section> toSections(List<SectionEdge> sectionEdges) {
        return sectionEdges.stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());
    }

    private Section getSection() {
        return section;
    }

    @Override
    protected double getWeight() {
        return section.getDistance();
    }

    @Override
    protected Station getSource() {
        return section.getUpStation();
    }

    @Override
    protected Station getTarget() {
        return section.getDownStation();
    }
}
