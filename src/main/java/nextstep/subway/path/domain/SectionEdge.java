package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

class SectionEdge extends DefaultWeightedEdge {

    private final Section section;

    SectionEdge(Section section) {
        this.section = section;
    }

    @Override
    protected double getWeight() {
        return section.getDistanceValue();
    }

    @Override
    protected Object getSource() {
        return section.getUpStation();
    }

    @Override
    protected Object getTarget() {
        return section.getDownStation();
    }

    public Line getLine() {
        return section.getLine();
    }
}
