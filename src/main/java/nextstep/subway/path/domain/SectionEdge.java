package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Section;

public class SectionEdge extends DefaultWeightedEdge {
    private Section section;

    private SectionEdge() {
    }

    private SectionEdge(Section section) {
        this.section = section;
    }

    public static SectionEdge from(Section section) {
        return new SectionEdge(section);
    }

    public Section getSection() {
        return section;
    }

    @Override
    protected Object getSource() {
        return section.getUpStation();
    }
    
    @Override
    protected Object getTarget() {
        return section.getDownStation();
    }
    
    @Override
    protected double getWeight() {
        return section.getDistance().getDistance();
    }

}
