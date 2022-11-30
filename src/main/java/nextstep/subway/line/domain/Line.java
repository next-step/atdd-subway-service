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

    @Embedded
    private LineName name;

    @Embedded
    private LineColor color;

    @Embedded
    private LineFare fare;

    @Embedded
    private Sections sections = Sections.createEmpty();

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
        this.fare = LineFare.zero();
    }

    public Line(String name, String color, int fare) {
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
        this.fare = LineFare.from(fare);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
        this.fare = LineFare.zero();
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int fare) {
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
        this.fare = LineFare.from(fare);
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = LineName.from(line.getName());
        this.color = LineColor.from(line.getColor());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getColor() {
        return color.getColor();
    }

    public LineFare getFare() {
        return fare;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public List<Station> getStations() {
        return this.sections.getStations();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public void removeSection(Station station) {
        this.sections.remove(station);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) &&
                Objects.equals(name, line.name) &&
                Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
