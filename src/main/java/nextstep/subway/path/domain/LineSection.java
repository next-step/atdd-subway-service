package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class LineSection extends DefaultWeightedEdge {
    private Section section;
    private Line line;

    public LineSection(Section section, Line line) {
        this.section = section;
        this.line = line;
    }

    public Section getSection() {
        return section;
    }

    public int getLineFare() {
        return line.getFare();
    }

    public int getDistance() {
        return section.distance();
    }

    @Override
    protected Object getSource() {
        return this.section.upStation();
    }

    @Override
    protected Object getTarget() {
        return this.section.downStation();
    }

    @Override
    protected double getWeight() {
        return this.section.distance();
    }

}
