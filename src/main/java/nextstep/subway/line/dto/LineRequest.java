package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;

import java.math.BigDecimal;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private BigDecimal extraCharge;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraCharge = BigDecimal.ZERO;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, BigDecimal extraCharge) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraCharge = extraCharge;
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

    public Line toLine() {
        return new Line(name, color);
    }

    public BigDecimal getExtraCharge() {
        return extraCharge;
    }
}
