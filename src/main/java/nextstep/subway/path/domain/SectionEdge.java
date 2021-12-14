package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private Station source;
    private Station target;
    private Distance distance;
    private Line line;

    public SectionEdge(Section section) {
        this.source = section.getUpStation();
        this.target = section.getDownStation();
        this.distance = section.getDistance();
        this.line = section.getLine();
    }

    public static SectionEdge of(Section section) {
        return new SectionEdge(section);
    }

    @Override
    protected double getWeight() {
        return Double.valueOf(distance.getValue());
    }

    @Override
    protected Station getSource() {
        return this.source;
    }

    @Override
    protected Station getTarget() {
        return this.target;
    }

    public Line getLine() {
        return line;
    }
}
