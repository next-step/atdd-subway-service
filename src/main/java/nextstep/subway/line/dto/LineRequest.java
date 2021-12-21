package nextstep.subway.line.dto;

import java.math.BigDecimal;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private BigDecimal extraFare;

    public LineRequest() {
    }

    public LineRequest(final String name, final String color, final Long upStationId, final Long downStationId, final int distance, final BigDecimal extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public static LineRequest of(final String name, final String color, final Long upStationId, final Long downStationId, final int distance, final int extraFare) {
        return new LineRequest(name, color, upStationId, downStationId, distance, new BigDecimal(extraFare));
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

    public BigDecimal getExtraFare() {
        return extraFare;
    }
}
