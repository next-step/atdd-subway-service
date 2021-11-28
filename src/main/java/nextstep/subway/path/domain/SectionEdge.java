package nextstep.subway.path.domain;

import nextstep.subway.line.domain.section.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private Station source;
    private Station target;
    private double weight;

    public SectionEdge() {}

    public SectionEdge(Station source, Station target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public static SectionEdge of(Section section) {
        return new SectionEdge(section.getUpStation(), section.getDownStation(), section.getDistance());
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
}
