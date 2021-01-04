package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class LineRequest {
    @NotNull(message = "노선 이름은 필수값입니다.")
    private String name;
    @NotNull(message = "노선 색상은 필수값입니다.")
    private String color;
    @NotNull(message = "노선 상생종점역은 필수값입니다.")
    private Long upStationId;
    @NotNull(message = "노선 하행종점역은 필수값입니다.")
    private Long downStationId;
    @NotNull(message = "종점역 간 거리는 필수값입니다.")
    private int distance;
    private BigDecimal extraFee;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this(name, color, upStationId, downStationId, distance, null);
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, BigDecimal extraFee) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFee = extraFee;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
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

    public BigDecimal getExtraFee() {
        return extraFee;
    }

    public Line toLine() {
        return new Line(name, color, extraFee);
    }
}
