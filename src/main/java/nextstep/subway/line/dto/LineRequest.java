package nextstep.subway.line.dto;

import lombok.NoArgsConstructor;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

@NoArgsConstructor
public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int extraCharge;

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int extraCharge) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraCharge = extraCharge;
    }

    public void useExtraCharge(int extraCharge) {
        this.extraCharge = extraCharge;
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

    public Line toLine(Station upStation, Station downStation) {
        return Line.of(name, color, upStation, downStation, distance, extraCharge);
    }
}
