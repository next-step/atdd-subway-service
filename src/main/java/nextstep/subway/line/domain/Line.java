package nextstep.subway.line.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private int extraFare;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {

    }

    private Line(Long id, String name, String color, int extraFare, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;

        sections.forEach(this::addSection);
    }

    public static Line of(String name, String color, int extraFare, Section section) {
        return new Line(null, name, color, extraFare, Collections.singletonList(section));
    }

    public static Line of(Long id, String name, String color, int extraFare, List<Section> sections) {
        return new Line(id, name, color, extraFare, sections);
    }

    public void addSection(Section section) {
        sections.add(section);
        section.setLine(this);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void removeSectionBy(Station station) {
        sections.removeBy(station);
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

    public int getExtraFare() {
        return extraFare;
    }

    public Sections getSections() {
        return sections;
    }

    public Stations getStations() {
        return sections.getStations();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Line line = (Line)o;
        return id.equals(line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
