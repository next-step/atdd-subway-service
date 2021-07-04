package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeightedEdge extends DefaultWeightedEdge {

    private final Section section;
    private final Long lineId;

    public SectionWeightedEdge(Section section, Long lineId) {
        this.section = section;
        this.lineId = lineId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Section getSection() {
        return section;
    }

    public int sectionDistance() {
        return section.getDistance().distance();
    }
}
