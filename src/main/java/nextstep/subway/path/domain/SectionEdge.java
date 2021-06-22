package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Section;

public class SectionEdge extends DefaultWeightedEdge {

    private final Section section;

    private SectionEdge(Section section) {
        this.section = section;
    }

    public static SectionEdge of(Section section) {
        return new SectionEdge(section);
    }

    public int getDistance() {
        return section.getDistance();
    }
}
