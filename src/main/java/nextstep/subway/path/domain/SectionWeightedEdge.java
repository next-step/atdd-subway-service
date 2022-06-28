package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeightedEdge extends DefaultWeightedEdge {
    private Section section;

    public SectionWeightedEdge(Section section) {
        this.section = section;
    }

    public Section getSection() {
        return this.section;
    }
}
