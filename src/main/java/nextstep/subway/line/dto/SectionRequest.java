package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private Distance distance;

    private SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, Distance distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance.get();
    }

    public Section toSection(Station upStation, Station downStation) {
        return new Section(upStation, downStation, distance);
    }
}
