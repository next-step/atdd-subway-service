package nextstep.subway.line.domain;


import nextstep.subway.BaseEntity;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    @Embedded
    private Sections sections = new Sections();

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "extra_fare"))
    private Fare extraFare;

    public static class Builder {
        private String name;
        private String color;
        private Station upStation;
        private Station downStation;
        private Distance distance;
        private Fare extraFare;

        public Builder(String name, String color) {
            this.name = name;
            this.color = color;
            this.extraFare = Fare.from(0);
        }

        public Builder upStation(Station station) {
            this.upStation = station;
            return this;
        }

        public Builder downStation(Station station) {
            this.downStation = station;
            return this;
        }

        public Builder distance(Distance distance) {
            this.distance = distance;
            return this;
        }

        public Builder extraFare(Fare extraFare) {
            this.extraFare = extraFare;
            return this;
        }

        public Line build() {
            return new Line(name, color, upStation, downStation, distance, extraFare);
        }
    }

    public Line() {
    }


    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance, Fare extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        sections.add(
                new Section.Builder()
                        .line(this)
                        .upStation(upStation)
                        .downStation(downStation)
                        .distance(distance)
                        .build()
        );
    }


    public Line(String name, String color, Station upStation, Station downStation, Distance distance) {
        this.name = name;
        this.color = color;
        this.extraFare = Fare.from(0);

        sections.add(
                new Section.Builder()
                        .line(this)
                        .upStation(upStation)
                        .downStation(downStation)
                        .distance(distance)
                        .build()
        );
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void addSection(Station upStation, Station downStation, Distance distance) {
        sections.add(
                new Section.Builder()
                        .line(this)
                        .upStation(upStation)
                        .downStation(downStation)
                        .distance(distance)
                        .build()
        );
    }

    public void removeStation(Station station) {
        sections.removeStation(station);
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Fare getExtraFare() {
        return extraFare;
    }
}
