package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.ExtraCharge;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int extraCharge;

    private LineRequest() {}

    private LineRequest(Builder builder) {
        this.name = builder.name;
        this.color = builder.color;
        this.upStationId = builder.upStationId;
        this.downStationId = builder.downStationId;
        this.distance = builder.distance;
        this.extraCharge = builder.extraCharge;
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
        Section section = new Section(upStation, downStation, new Distance(distance));
        Line line = new Line(name, color, new ExtraCharge(extraCharge));
        line.addSection(section);

        return line;
    }

    public static class Builder {
        private String name;
        private String color;
        private Long upStationId;
        private Long downStationId;
        private int distance;
        private int extraCharge;

        public Builder() {}

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder upStationId(Long upStationId) {
            this.upStationId = upStationId;
            return this;
        }

        public Builder downStationId(Long downStationId) {
            this.downStationId = downStationId;
            return this;
        }

        public Builder distance(int distance) {
            this.distance = distance;
            return this;
        }

        public Builder extraCharge(int extraCharge) {
            this.extraCharge = extraCharge;
            return this;
        }

        public LineRequest build() {
            return new LineRequest(this);
        }
    }
}
