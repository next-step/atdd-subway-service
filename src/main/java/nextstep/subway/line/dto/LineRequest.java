package nextstep.subway.line.dto;

public class LineRequest {
    private String name;
    private String color;
    private Integer surcharge;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Integer surcharge, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.surcharge = surcharge;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this(name, color, 0, upStationId, downStationId, distance);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getSurcharge() {
        return surcharge;
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
}
