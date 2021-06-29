package nextstep.subway.line.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    protected Object source;
    protected Object target;
    protected double weight;
    protected int additionalFare;

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

    public int getAdditionalFare() {
        return additionalFare;
    }

}
