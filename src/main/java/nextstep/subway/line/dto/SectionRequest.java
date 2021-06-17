package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Distance;

public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private Distance distance;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = new Distance(distance);
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
}
