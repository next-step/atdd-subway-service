package nextstep.subway.line.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private Section section;
    private int extraFare;

    public SectionEdge(Section section, int extraFare) {
        this.section = section;
        this.extraFare = extraFare;
    }

    public int getExtraFare() {
        return extraFare;
    }

    @Override
    protected Object getSource() {
        return section.getUpStation();
    }

    @Override
    protected Object getTarget() {
        return section.getDownStation();
    }

    @Override
    protected double getWeight() {
        return section.getDistance();
    }

}
