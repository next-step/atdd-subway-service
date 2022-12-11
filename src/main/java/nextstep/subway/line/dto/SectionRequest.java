package nextstep.subway.line.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

import nextstep.subway.line.domain.Distance;

public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private Distance distance;

    public SectionRequest() {
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

    public Distance getDistance() {
        return distance;
    }

    @JsonGetter("distance")
    public Integer getDistanceJson() {
        return distance.getValue();
    }

}
