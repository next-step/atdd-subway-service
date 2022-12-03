package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private final Section section;

    public SectionEdge(Section section) {
        this.section = section;
    }

    public Section getSection() {
        return section;
    }

    public Line getLine() {
        return section.getLine();
    }

    @Override
    protected Object getSource() {
        return this.section.getUpStation();
    }

    @Override
    protected Object getTarget() {
        return this.section.getDownStation();
    }

    @Override
    protected double getWeight() {
        return this.section.getDistance().value();
    }
}
