package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int extraFare;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this(name, color, upStationId, downStationId, distance, 0);
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
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

    public Line toLine() {
        return new Line(name, color);
    }

    public int getExtraFare() {
        return extraFare;
    }

    public Line toLine(Station upStation, Station downStation) {
        return new Line(name, color, upStation, downStation, distance);
    }

    public static class Builder {
        private LineRequest lineRequest = new LineRequest();

        public Builder name(String name) {
            lineRequest.name = name;
            return this;
        }

        public Builder color(String color) {
            lineRequest.color = color;
            return this;
        }

        public Builder upStationId(Long upStationId) {
            lineRequest.upStationId = upStationId;
            return this;
        }

        public Builder downStationId(Long downStationId) {
            lineRequest.downStationId = downStationId;
            return this;
        }

        public Builder distance(int distance) {
            lineRequest.distance = distance;
            return this;
        }

        public Builder extraFare(int extraFare) {
            lineRequest.extraFare = extraFare;
            return this;
        }

        public LineRequest build() {
            return lineRequest;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
