package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class LineSection extends DefaultWeightedEdge {
    private Section section;
    private Long lineId;

    public LineSection(Section section, Long lineId) {
        this.section = section;
        this.lineId = lineId;
    }

    public Section getSection() {
        return section;
    }

    public Long getLineId() {
        return lineId;
    }

    public int getDistance() {
        return section.distance();
    }

    @Override
    protected Object getSource() {
        return this.section.upStation();
    }

    @Override
    protected Object getTarget() {
        return this.section.downStation();
    }

    @Override
    protected double getWeight() {
        return this.section.distance();
    }

}
