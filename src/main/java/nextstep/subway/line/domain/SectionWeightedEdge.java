package nextstep.subway.line.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeightedEdge extends DefaultWeightedEdge {
    private final Section section;

    public SectionWeightedEdge(final Section section) {
        this.section = section;
    }

    public Line getLine() {
        return section.getLine();
    }

    public int getSurcharge() {
        return section.getLine().getSurcharge();
    }
}
