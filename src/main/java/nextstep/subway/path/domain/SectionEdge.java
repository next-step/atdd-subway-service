package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private Section section;

    public SectionEdge() {
    }

    public SectionEdge(Section section) {
        this.section = section;
    }

    public void addSection(Section section) {
        this.section = section;
    }

    public Line getLine() {
        return this.section.getLine();
    }

}
