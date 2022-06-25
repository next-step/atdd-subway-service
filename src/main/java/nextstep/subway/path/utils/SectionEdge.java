package nextstep.subway.path.utils;

import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private Section section;

    public SectionEdge() {
        super();
    }

    public SectionEdge updateSection(Section section) {
        this.section = section;
        return this;
    }

    public Section getSection() {
        return section;
    }

}
