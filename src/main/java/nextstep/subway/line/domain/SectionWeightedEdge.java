package nextstep.subway.line.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeightedEdge extends DefaultWeightedEdge {

    private Line line;

    public SectionWeightedEdge(Section section) {
        this.line = section.getLine();
    }

    public int getExtraFare() {
        return line.getExtraFare();
    }
}
