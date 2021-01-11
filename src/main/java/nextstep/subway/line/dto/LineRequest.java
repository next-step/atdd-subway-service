package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Money;
import nextstep.subway.station.domain.Station;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private long additionalFare;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, long additionalFare) {
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

    public long getAdditionalFare() {
        return additionalFare;
    }

    public Line toLine() {
        return new Line(name, color, Money.won(additionalFare));
    }

    public Line toLine(Station upStation, Station downStation) {
        return new Line(name, color, upStation, downStation, Money.won(additionalFare), distance);
    }
}
