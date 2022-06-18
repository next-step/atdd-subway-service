package nextstep.subway.line.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;

    SectionEdge(Section section) {
        this.section = section;
    }

    @Override
    protected double getWeight() {
        return section.getDistance().getValue();
    }

    @Override
    protected Object getSource() {
        return section.getUpStation();
    }

    @Override
    protected Object getTarget() {
        return section.getDownStation();
    }

    public Line getLine() {
        return section.getLine();
    }
}
