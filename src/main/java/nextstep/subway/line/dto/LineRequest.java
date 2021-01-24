package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;

import javax.persistence.criteria.CriteriaBuilder;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;
    private Integer addFee;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, Integer distance, Integer addFee) {
        new LineRequest(name, color, upStationId, downStationId, distance);
        this.addFee = addFee;
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

    public Integer getDistance() {
        return distance;
    }

    public Integer getAddFee() {
        return addFee;
    }

    public Line toLine() {
        return new Line(name, color);
    }
}
