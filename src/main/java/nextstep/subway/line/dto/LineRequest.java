package nextstep.subway.line.dto;

import nextstep.subway.station.dto.StationResponse;

public class LineRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int surcharge = 0;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineRequest(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStation.getId();
        this.downStationId = downStation.getId();
        this.distance = distance;
    }

    public LineRequest(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int surcharge) {
        this.name = name;
        this.color = color;
        this.upStationId = upStation.getId();
        this.downStationId = downStation.getId();
        this.distance = distance;
        this.surcharge = surcharge;
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

    @Override
    public String toString() {
        return "LineRequest{" +
            "name='" + name + '\'' +
            ", color='" + color + '\'' +
            ", upStationId=" + upStationId +
            ", downStationId=" + downStationId +
            ", distance=" + distance +
            ", surcharge=" + surcharge +
            '}';
    }
}
