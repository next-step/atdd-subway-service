package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.util.Assert;

final class SectionEdge extends DefaultWeightedEdge {

    private final Section section;

    private SectionEdge(Section section) {
        Assert.notNull(section, "구간은 필수입니다.");
        this.section = section;
    }

    static SectionEdge from(Section section) {
        return new SectionEdge(section);
    }

    Section section() {
        return section;
    }

    @Override
    protected Object getSource() {
        return section.upStation();
    }

    @Override
    protected Object getTarget() {
        return section.downStation();
    }

    @Override
    protected double getWeight() {
        return section.distanceValue();
    }
}
