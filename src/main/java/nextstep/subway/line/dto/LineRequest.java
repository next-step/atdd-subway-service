package nextstep.subway.line.dto;

import java.util.Objects;

public class LineRequest {

    private final static long INIT_STATION_ID = 0L;
    private String name;
    private String color;
    private long upStationId;
    private long downStationId;
    private int distance;

    private int fare;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int fare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.fare = fare;
    }

    public static void saveLineValidate(LineRequest lineRequest) {
        if (Objects.isNull(lineRequest)) {
            throw new IllegalArgumentException("요청한 노선이 존재하지 않습니다.");
        }
        if (isStationIdInitValue(lineRequest.getUpStationId())) {
            throw new IllegalArgumentException("upStationId 값은 유효하지 않습니다");
        }
        if (isStationIdInitValue(lineRequest.getDownStationId())) {
            throw new IllegalArgumentException("downStation 값은 유효하지 않습니다");
        }
    }

    private static boolean isStationIdInitValue(long stationId) {
        return stationId == INIT_STATION_ID;
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
