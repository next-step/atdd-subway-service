package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;

public class LineSection {
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
}
