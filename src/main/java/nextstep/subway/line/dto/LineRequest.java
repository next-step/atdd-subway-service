package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class LineRequest {

    private static final int ZERO = 0;

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int lineFare;

    private LineRequest() {}

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this(name, color, upStationId, downStationId, distance, ZERO);
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int lineFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.lineFare = lineFare;
    }

    public Line toLine(Station upStation, Station downStation) {
        return new Line(name, color, upStation, downStation, distance, lineFare);
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

    public int getLineFare() {
        return lineFare;
    }
}
