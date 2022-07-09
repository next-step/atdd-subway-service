package nextstep.subway.line.dto;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int surcharge;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int surcharge) {
        this(name, color, upStationId, downStationId, distance);
        this.surcharge = surcharge;
    }

    public static LineRequest of(String name, String color, long upStationId, long downStationId, int distance) {
        return new LineRequest(name, color, upStationId, downStationId, distance);
    }

    public static LineRequest of(String name, String color, long upStationId, long downStationId, int distance, int surcharge) {
        return new LineRequest(name, color, upStationId, downStationId, distance, surcharge);
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

    public int getSurcharge() {
        return surcharge;
    }

}
