package nextstep.subway.line.domain;

import java.util.Collections;
import java.util.Set;
import nextstep.subway.BaseEntity;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String color;

    @Embedded
    private Sections sections;

    @Embedded
    private Fare extraFare;

    protected Line() {

    }

    private Line(Builder builder) {
        this.name = builder.name;
        this.color = builder.color;
        this.extraFare = new Fare(builder.extraFare);
        Section section = new Section(builder.upStation, builder.downStation, builder.distance);
        section.addLine(this);
        this.sections = new Sections(Collections.singletonList(section));
    }

    public static Builder builder() {
        return new Builder();
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeColor(String color) {
        this.color = color;
    }

    public Set<Station> findAssignedStations() {
        return sections.assignedOrderedStation();
    }

    public void addSection(Section newSection) {
        newSection.addLine(this);
        this.sections.add(newSection);
    }

    public void deleteStation(Station station) {
        this.sections.delete(station);
    }

    public Sections getSections() {
        return this.sections;
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

    public static class Builder {
        private String name;
        private int distance;
        private int extraFare;
        private String color;
        private Station upStation;
        private Station downStation;

        private Builder() {

        }

        public Builder name(String name) {
            this.name = name;
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

        public Line build() {
            return new Line(this);
        }
    }
}
