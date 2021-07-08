package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fee;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Distance distance;
    private Fee fee;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = new Distance(distance);
        this.fee = Fee.of(0);
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int fee) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = new Distance(distance);
        this.fee = Fee.of(fee);
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
        return distance.get();
    }

    public int getFee() {
        return fee.get();
    }
}