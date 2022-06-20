package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
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

    @Embedded
    private Sections sections = Sections.empty();

    public static class Builder {
        private String name;
        private String color;
        private Station upStation;
        private Station downStation;
        private int distance;

        public Builder(String name, String color) {
            this.name = name;
            this.color = color;
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

        public Line build() {
            return new Line(this);
        }
    }

    protected Line() {}

    private Line(Builder builder) {
        this.name = builder.name;
        this.color = builder.color;
        if (isAddedSection(builder)) {
            this.addSection(Section.of(builder.upStation, builder.downStation, builder.distance));
        }
    }

    private boolean isAddedSection(Builder builder) {
        return !Objects.isNull(builder.upStation)
                && !Objects.isNull(builder.downStation)
                && builder.distance > 0;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
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
