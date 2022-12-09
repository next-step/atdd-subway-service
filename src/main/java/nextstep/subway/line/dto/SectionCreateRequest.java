package nextstep.subway.line.dto;

public class SectionCreateRequest {
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public SectionCreateRequest(Long upStationId, Long downStationId, int distance) {
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
        return distance;
    }
}