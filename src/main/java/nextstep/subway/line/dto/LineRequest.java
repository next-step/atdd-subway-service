package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class LineRequest {
    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;
    private final int extraCharge;

    public LineRequest(final String name, final String color, final Long upStationId, final Long downStationId, final int distance, final int extraCharge) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraCharge = extraCharge;
    }

    public Line toEntity(final Station upStation, final Station downStation) {
        return new Line(name, color, upStation, downStation, distance, extraCharge);
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

    public int getExtraCharge() {
        return extraCharge;
    }

    public Line toLine() {
        return new Line(name, color);
    }
}
