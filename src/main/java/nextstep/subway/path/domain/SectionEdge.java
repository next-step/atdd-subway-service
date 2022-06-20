package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;

    public SectionEdge(Section section) {
        this.section = section;
    }

    @Override
    protected Station getSource() {
        return this.section.getUpStation();
    }

    @Override
    protected Station getTarget() {
        return this.section.getDownStation();
    }

    public Line getLine() {
        return this.section.getLine();
    }
}
