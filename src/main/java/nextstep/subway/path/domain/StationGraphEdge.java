package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class StationGraphEdge extends DefaultWeightedEdge {
    private final Section section;

    public StationGraphEdge(Section section) {
        this.section = section;
    }

    public static StationGraphEdge from(Section section) {
        return new StationGraphEdge(section);
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

    public Line getLine() {
        return section.getLine();
    }
}
