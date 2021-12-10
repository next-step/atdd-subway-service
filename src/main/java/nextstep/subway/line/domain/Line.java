package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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

    protected Line() {
    }

    private Line(final String name, final String color, final Sections sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    private Line(final String name, final String color) {
        this(name, color, Sections.empty());
    }

    public static Line of(final String name, final String color, final Sections sections) {
        return new Line(name, color, sections);
    }

    public static Line of(final String name, final String color) {
        return new Line(name, color);
    }

    public void update(final Line line) {
        this.name = line.getName();
        this.color = line.getColor();
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

    public List<Station> getStations() {
        return this.sections.getStations();
    }

    public Sections getSections() {
        return this.sections;
    }

    public List<Section> getSectionList() {
        return this.sections.getSections();
    }

    public void addSection(final Section section) {
        this.sections.add(section, this);
    }

    public boolean isContainStation(final Station station){
        return this.sections.contains(station);
    }

    public void removeSectionByStationId(final Station station) {
        this.sections.merge(station);
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
