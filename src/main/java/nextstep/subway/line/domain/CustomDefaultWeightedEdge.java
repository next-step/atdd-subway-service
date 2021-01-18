package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import org.jgrapht.graph.DefaultWeightedEdge;

public class CustomDefaultWeightedEdge extends DefaultWeightedEdge {
    @Override
    public double getWeight() {
        return super.getWeight();
    }

    @Override
    public Station getSource() {
        return super.getSource() instanceof Station ? (Station)super.getSource() : null;
    }

    @Override
    protected Station getTarget() {
        return super.getTarget() instanceof Station ? (Station)super.getTarget() : null;
    }
}
