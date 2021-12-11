package nextstep.subway.path.infrastructure;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private Station source;
    private Station target;
    private Integer weight;
    private Fare additionalFare;

    public SectionEdge(Section section, Line line) {
        this.source = section.getUpStation();
        this.target = section.getDownStation();
        this.weight = section.getDistance().get();
        this.additionalFare = line.getAdditionalFare();
    }

    public Fare getAdditionalFare() {
        return additionalFare;
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
