package nextstep.subway.line.dto;

import nextstep.subway.line.domain.line.Line;

import java.math.BigDecimal;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private BigDecimal fare;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.fare = BigDecimal.ZERO;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, BigDecimal fare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.fare = fare;
    }

    public static LineRequest of(String name, String color, Long upStationId, Long downStationId, int distance, BigDecimal fare) {
        return new LineRequest(name, color, upStationId, downStationId, distance, fare);
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

    public BigDecimal getFare() {
        return fare;
    }

    public Line toLine() {
        return new Line(name, color, fare);
    }
}
