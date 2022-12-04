package nextstep.subway.line.domain;

import java.util.Collections;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Color color;

    @Embedded
    private Sections sections;

    @Embedded
    private ExtraFare extraFare;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = Name.from(name);
        this.color = Color.from(color);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int extraFare) {
        Section section = Section.of(this, upStation, downStation, distance);
        this.name = Name.from(name);
        this.color = Color.from(color);
        this.sections = Sections.from(Collections.singletonList(section));
        this.extraFare = ExtraFare.from(extraFare);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color, upStation, downStation, distance, ExtraFare.ZERO);
    }

    public Line(Builder builder) {
        this(builder.name, builder.color, builder.upStation, builder.downStation, builder.distance, builder.extraFare);
    }

    public void update(String name, String color) {
        this.name = Name.from(name);
        this.color = Color.from(color);
    }

    public void addSection(Section section) {
        sections.add(section);
        section.belong(this);
    }

    public void removeSection(Station station) {
        sections.remove(station);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public Sections getSections() {
        return sections;
    }

    public ExtraFare getExtraFare() {
        return extraFare;
    }

    public static class Builder {
        private String name;
        private String color;
        private Station upStation;
        private Station downStation;
        private int distance;
        private int extraFare;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder upStation(Station upStation) {
            this.upStation = upStation;
            return this;
        }

        public Builder downStation(Station downStation) {
            this.downStation = downStation;
            return this;
        }

        public Builder distance(int distance) {
            this.distance = distance;
            return this;
        }

        public Builder extraFare(int extraFare) {
            this.extraFare = extraFare;
            return this;
        }

        public Line build() {
            return new Line(this);
        }
    }
}
