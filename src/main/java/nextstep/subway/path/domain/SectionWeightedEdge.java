package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeightedEdge extends DefaultWeightedEdge {
    private Section section;

    public SectionWeightedEdge(Section section) {
        this.section = section;
    }

    @Override
    protected double getWeight() {
        return weight(this.section.getDistance());
    }

    private double weight(Distance distance) {
        return distance.getDistance();
    }

    @Override
    protected Object getSource() {
        return this.section.upStation();
    }

    @Override
    protected Object getTarget() {
        return this.section.downStation();
    }

    public Line getLine() {
        return this.section.getLine();
    }
}
