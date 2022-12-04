package nextstep.subway.line.dto;

import static nextstep.subway.line.domain.Line.DEFAULT_LINE_SURCHARGE;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int lineSurcharge;

    private LineRequest() {}

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this(name, color, upStationId, downStationId, distance, DEFAULT_LINE_SURCHARGE);
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int lineSurcharge) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.lineSurcharge = lineSurcharge;
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

    public int getLineSurcharge() {
        return lineSurcharge;
    }

    public Line toLine(Station upStation, Station downStation) {
        return Line.of(name, color, lineSurcharge, Section.of(upStation, downStation, Distance.from(distance)));
    }
}
