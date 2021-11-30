package nextstep.subway.path.domain;

import nextstep.subway.line.domain.line.Line;
import nextstep.subway.line.domain.section.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private Line line;
    private Station source;
    private Station target;
    private double weight;

    public SectionEdge() {}

    public SectionEdge(Line line, Station source, Station target, double weight) {
        this.line = line;
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public static SectionEdge of(Section section) {
        return new SectionEdge(section.getLine(), section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public Station getSource() {
        return source;
    }

    @Override
    public Station getTarget() {
        return target;
    }

    public Line getLine() {
        return line;
    }
}
