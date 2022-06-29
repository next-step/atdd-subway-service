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
    protected double getWeight() {
        return section.getDistanceValue();
    }

    @Override
    protected Station getSource() {
        return section.getUpStation();
    }

    @Override
    protected Station getTarget() {
        return section.getDownStation();
    }

    public Line getLine() {
        return section.getLine();
    }
}
