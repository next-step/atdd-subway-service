package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;

public class SectionEdge extends DefaultWeightedEdge {

    private final Line line;
    private final int distance;

    public static SectionEdge of(Section section) {
        return new SectionEdge(section.getLine(), section.getDistance());
    }

    public SectionEdge(Line line, int distance) {
        this.line = line;
        this.distance = distance;
    }

    public Line getLine() {
        return line;
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
