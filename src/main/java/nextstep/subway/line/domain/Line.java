package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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
    private Sections sections = Sections.initSections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = Name.from(name);
        this.color = Color.from(color);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = Name.from(name);
        this.color = Color.from(color);
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name =  Name.from(line.getName());
        this.color = Color.from(line.getColor());
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

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void addSection(Section section) {
        this.sections.add(section);
        section.updateLine(this);
    }

    public void removeSection(Station station) {
        this.sections.remove(station);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color) && Objects.equals(sections, line.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, sections);
    }

    public List<Section> getSections() {
        return sections.getSections();
    }
}
