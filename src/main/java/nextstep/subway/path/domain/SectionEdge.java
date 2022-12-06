package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;

    private SectionEdge(Section section) {
        this.section = section;
    }

    static SectionEdge from(Section section) {
        return new SectionEdge(section);
    }

    @Override
    protected double getWeight() {
        return section.distanceValue();
    }

    @Override
    protected Object getSource() {
        return section.upStation();
    }

    @Override
    protected Object getTarget() {
        return section.downStation();
    }

    public Line getLine() {
        return section.line();
    }
}
