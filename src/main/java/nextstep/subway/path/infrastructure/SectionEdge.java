package nextstep.subway.path.infrastructure;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private Station source;
    private Station target;
    private Integer weight;
    private Line line;

    public SectionEdge(Section section, Line line) {
        this.source = section.getUpStation();
        this.target = section.getDownStation();
        this.weight = section.getDistance().get();
        this.line = line;
    }

    public Line getLine() {
        return line;
    }

    @Override
    protected double getWeight() {
        return Double.valueOf(this.weight);
    }

    @Override
    protected Station getSource() {
        return this.source;
    }

    @Override
    protected Station getTarget() {
        return this.target;
    }
}
