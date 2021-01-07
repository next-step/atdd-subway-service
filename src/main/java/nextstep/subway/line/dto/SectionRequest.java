package nextstep.subway.line.dto;

import java.util.Objects;

public class SectionRequest {
    private static final int MIN_DISTANCE = 0;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    protected SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, int distance) {
        validateStations(upStationId, downStationId);
        validateDistance(distance);
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

    private void validateStations(Long upStationId, Long downStationId) {
        if(Objects.isNull(upStationId) && Objects.isNull(downStationId)){
            throw new IllegalArgumentException("추가하려는 구간의 상행역과 하행역 모두 선택해야 합니다.");
        }
    }

    private void validateDistance(int distance) {
        if(distance == MIN_DISTANCE) {
            throw new IllegalArgumentException("추가되는 구간의 거리는 0이 될 수 없습니다.");
        }
    }
}
