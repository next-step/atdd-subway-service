package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class LineRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;
    private Integer additionalFare;

    public LineRequest() {
    }

    public LineRequest(String name, String color, int additionalFare) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
    }

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
        return Line.of(name, color, upStation, downStation, distance, additionalFare);
    }

    public Line toLine() {
        return Line.of(name, color, additionalFare);
    }

    public boolean hasSection() {
        return upStationId != null || downStationId != null || distance != null;
    }
}
