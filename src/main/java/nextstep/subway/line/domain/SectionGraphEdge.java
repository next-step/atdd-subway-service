package nextstep.subway.line.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionGraphEdge extends DefaultWeightedEdge {
    private Section section;

    private SectionGraphEdge() {
    }

    private SectionGraphEdge(Section section) {
        this.section = section;
    }

    public static SectionGraphEdge of(Section section) {
        return new SectionGraphEdge(section);
    }

    public Section getSection() {
        return section;
    }
}
