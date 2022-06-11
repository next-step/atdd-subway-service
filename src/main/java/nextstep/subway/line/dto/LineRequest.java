package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;

public class LineRequest {
    private String name;
    private String color;
    private int extraFare;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    protected LineRequest() {
    }

    public LineRequest(String name, String color, int extraFare, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static LineRequest of(String name, String color, int extraFare, Long upStationId, Long downStationId, int distance) {
        return new LineRequest(name, color, extraFare, upStationId, downStationId, distance);
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

    public int getExtraFare() {
        return extraFare;
    }

    public Line toLine() {
        return new Line(name, color, extraFare);
    }
}
