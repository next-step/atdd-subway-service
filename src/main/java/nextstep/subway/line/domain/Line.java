package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private Fare extraFare;

    @Embedded
    private Sections sections = Sections.empty();

    public static class Builder {
        private String name;
        private String color;
        private int extraFare = 0;
        private Section section;

        public Builder(String name, String color) {
            this.name = name;
            this.color = color;
        }

        public Builder extraFare(int extraFare) {
            this.extraFare = extraFare;
            return this;
        }

        public Builder section(Section section) {
            this.section = section;
            return this;
        }

        public Line build() {
            return new Line(this);
        }
    }

    protected Line() {}

    private Line(Builder builder) {
        this.name = builder.name;
        this.color = builder.color;
        this.extraFare = Fare.from(builder.extraFare);
        if (isAddedSection(builder)) {
            this.addSection(builder.section);
        }
    }

    private boolean isAddedSection(Builder builder) {
        return !Objects.isNull(builder.section);
    }

    public void update(Line updateLine) {
        this.name = updateLine.name;
        this.color = updateLine.color;
        this.extraFare = updateLine.extraFare;
    }

    public void addSection(Section section) {
        this.sections.add(section);
        section.assignLine(this);
    }

    public void removeStation(Station station) {
        this.sections.removeStation(station);
    }

    public List<Station> getStations() {
        return this.sections.getStations();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    public int getExtraFare() {
        return this.extraFare.get();
    }

    public List<Section> getSections() {
        return this.sections.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
