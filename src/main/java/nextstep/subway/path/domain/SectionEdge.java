package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private final Section section;

    public SectionEdge(Section section) {
        this.section = section;
    }

    public static SectionEdge of(Section section) {
        return new SectionEdge(section);
    }

    public int getFare() {
        return this.section.getLine().getFare();
    }
}
