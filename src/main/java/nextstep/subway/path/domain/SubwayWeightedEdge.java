package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SubwayWeightedEdge extends DefaultWeightedEdge {
    private Section section;
    private Line line;

    public SubwayWeightedEdge(Section section, Line line) {
        this.section = section;
        this.line = line;
    }

    public Section getSection() {
        return section;
    }

    public Line getLine() {
        return line;
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
        return this.section.getDistance();
    }
}
