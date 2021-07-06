package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeightedEdge extends DefaultWeightedEdge {

    private final Section section;
    private final Long lineId;
    private final int extraFare;

    public SectionWeightedEdge(Section section, Long lineId, int extraFare) {
        this.section = section;
        this.lineId = lineId;
        this.extraFare = extraFare;
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

    public int getExtraFare() {
        return extraFare;
    }
}
