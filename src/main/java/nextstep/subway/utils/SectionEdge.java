package nextstep.subway.utils;

import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private Section section;

    public void setSection(Section section) {
        this.section = section;
    }

    public Section getSection() {
        return section;
    }
}

