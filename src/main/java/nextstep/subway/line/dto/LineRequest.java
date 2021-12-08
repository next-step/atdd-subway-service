package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int surcharge;

    public LineRequest() {
    }

    public LineRequest(String name, String color, int surcharge, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.surcharge = surcharge;
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

    public int getSurcharge() {
        return surcharge;
    }

    public Line toLine(Station upStation, Station downStation) {
        return new Line(name, color, surcharge, upStation, downStation, distance);
    }
}
