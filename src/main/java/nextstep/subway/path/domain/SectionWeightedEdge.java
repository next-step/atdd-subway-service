package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeightedEdge extends DefaultWeightedEdge {
    private final Section section;

    public SectionWeightedEdge(Section section) {
        this.section = section;
    }

    Line getLine() {
        return section.getLine();
    }
}
