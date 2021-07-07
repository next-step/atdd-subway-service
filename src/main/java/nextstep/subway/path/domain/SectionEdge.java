package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private Section section;

    public SectionEdge(final Section section) {
        this.section = section;
    }

    public int getAdditionalFare() {
        return section.getLine().getAdditionalFare();
    }
}
