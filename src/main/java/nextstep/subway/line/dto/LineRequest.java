package nextstep.subway.line.dto;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int fare;

    protected LineRequest() {}

    private LineRequest(String name, String color, int fare, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.fare = fare;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static LineRequest of(String name, String color, int fare, Long upStationId, Long downStationId, int distance) {
        return new LineRequest(name, color, fare, upStationId, downStationId, distance);
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

    public int getFare() {
        return fare;
    }
}
