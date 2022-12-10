package nextstep.subway.path.domain;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeightEdge extends DefaultWeightedEdge {

    private Station source;
    private Station target;
    private double weight;
    private Fare fare;

    public SectionWeightEdge(Section section) {
        this.source = section.getUpStation();
        this.target = section.getDownStation();
        this.weight = section.getDistance().distance();
        this.fare = section.getFare();
    }

    @Override
    protected Object getSource() {
        return source;
    }

    @Override
    protected Object getTarget() {
        return target;
    }

    @Override
    protected double getWeight() {
        return weight;
    }
}
