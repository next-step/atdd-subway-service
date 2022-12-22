package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private Double extraFare;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineRequest(String name, String color, Double extraFare, Long upStationId, Long downStationId,
            int distance) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineRequest(String name, String color, int extraFare, Long upStationId, Long downStationId, int distance) {
        this(name, color, Double.valueOf(extraFare), upStationId, downStationId, distance);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Double getExtraFare() {
        return extraFare;
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
}
