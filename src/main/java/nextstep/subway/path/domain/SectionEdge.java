package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

class SectionEdge extends DefaultWeightedEdge {

    private final int additionalPrice;

    private SectionEdge(int additionalPrice) {
        this.additionalPrice = additionalPrice;
    }

    public static SectionEdge of(Section section) {
        return new SectionEdge(section.getAdditionalPrice());
    }

    public int getAdditionalPrice() {
        return additionalPrice;
    }
}
