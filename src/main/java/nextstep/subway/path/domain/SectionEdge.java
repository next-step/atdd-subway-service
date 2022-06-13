package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultEdge;

public class SectionEdge extends DefaultEdge {
    private Section section;

    public void addSection(Section section) {
        this.section = section;
    }

    public Line getLine() {
        return section.getLine();
    }
}
