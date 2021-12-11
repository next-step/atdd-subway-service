package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class LineRequest {

    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;
    private final int additionalFare;

    public LineRequest(String name, String color, Long upStationId, Long downStationId,
        int distance,
        int additionalFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.additionalFare = additionalFare;
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

    public int getAdditionalFare() {
        return additionalFare;
    }

    public Line toLine(Station upStation, Station downStation) {
        return Line.of(name, color, upStation, downStation, distance);
    }
}
