package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private final Section section;

    public SectionEdge(Section section) {
        this.section = section;
    }

    @Override
    public double getWeight() {
        return section.getDistance().getDistance();
    }

    @Override
    public Object getSource() {
        return section.getUpStation();
    }

    @Override
    public Object getTarget() {
        return section.getDownStation();
    }

    public Line getLine() {
        return section.getLine();
    }
}
