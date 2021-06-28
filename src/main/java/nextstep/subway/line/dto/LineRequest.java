package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.vo.Amount;
import nextstep.subway.line.domain.Line;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    private long lineAdditionalFee;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, long lineAdditionalFee) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.lineAdditionalFee = lineAdditionalFee;
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

    public long getLineAdditionalFee() {
        return lineAdditionalFee;
    }

    public Distance toDistanceDomain() {
        return Distance.from(distance);
    }

    public Amount toFeeDomain() {
        return Amount.from(lineAdditionalFee);
    }

    public Line toLine() {
        return new Line(name, color);
    }
}
