package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;

import java.util.Objects;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    private LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineRequest)) return false;
        LineRequest request = (LineRequest) o;
        return distance == request.distance && Objects.equals(name, request.name) && Objects.equals(color, request.color) && Objects.equals(upStationId, request.upStationId) && Objects.equals(downStationId, request.downStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color, upStationId, downStationId, distance);
    }
}
