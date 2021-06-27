package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private int surcharge;

    public SectionEdge(int surcharge) {
        this.surcharge = surcharge;
    }

    public int getSurcharge() {
        return surcharge;
    }

    @Override
    public Station getSource() {
        return (Station) super.getSource();
    }

    @Override
    public Station getTarget() {
        return (Station) super.getTarget();
    }

    @Override
    public double getWeight() {
        return super.getWeight();
    }

    @Override
    public String toString() {
        return "SectionEdge{" +
            "source=" + super.getSource() +
            "target=" + super.getTarget() +
            "weight=" + super.getWeight() +
            "surcharge=" + this.surcharge +
            '}';
    }
}
