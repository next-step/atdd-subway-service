package nextstep.subway.line.dto;

import javax.validation.constraints.NotNull;

public class SectionRequest {
    @NotNull(message = "상행역은 필수값입니다.")
    private Long upStationId;
    @NotNull(message = "하행역은 필수값입니다.")
    private Long downStationId;
    @NotNull(message = "역간 거리는 필수값입니다.")
    private int distance;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, int distance) {
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
