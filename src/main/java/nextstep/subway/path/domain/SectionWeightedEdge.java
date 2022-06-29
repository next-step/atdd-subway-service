package nextstep.subway.path.domain;

import nextstep.subway.line.domain.ExtraCharge;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeightedEdge extends DefaultWeightedEdge {
    private final Section section;

    public SectionWeightedEdge(final Section section) {
        this.section = section;
    }

    public Section getSection() {
        return section;
    }

    public int getExtraCharge() {
        return section.getLine()
                .getExtraCharge();
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
        return section.getDistance();
    }
}
