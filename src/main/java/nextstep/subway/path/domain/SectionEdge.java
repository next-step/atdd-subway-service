package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private final Station source;
    private final Station target;
    private final int distance;
    private final Line line;

    private SectionEdge(final Section section) {
        this.source = section.getUpStation();
        this.target = section.getDownStation();
        this.distance = section.getDistance().getDistance();
        this.line = section.getLine();
    }

    public static SectionEdge of(final Section section) {
        return new SectionEdge(section);
    }

    public Line getLine() {
        return line;
    }

    @Override
    protected double getWeight() {
        return distance;
    }

    @Override
    protected Station getSource() {
        return source;
    }

    @Override
    protected Station getTarget() {
        return target;
    }
}
