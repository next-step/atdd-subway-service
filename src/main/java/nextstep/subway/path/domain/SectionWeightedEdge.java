package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeightedEdge extends DefaultWeightedEdge {

    private final Section section;
    private final Long lineId;
    private final Fare fare;

    public SectionWeightedEdge(Section section, Long lineId, int fare) {
        this.section = section;
        this.lineId = lineId;
        this.fare = new Fare(fare);
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

    public Fare getFare() {
        return fare;
    }
}
