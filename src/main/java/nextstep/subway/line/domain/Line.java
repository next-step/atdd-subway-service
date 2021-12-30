package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.math.BigDecimal;
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

    @Embedded
    private Fare extraFare;

    protected Line() {
    }

    public Line(final Long id, final String name, final String color, final Sections sections, final Fare extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
        this.extraFare = extraFare;
    }

    private Line(final String name, final String color, final Sections sections, final Fare extraFare) {
        this(null, name, color, sections, extraFare);
    }

    private Line(final String name, final String color, final Fare extraFare) {
        this(name, color, Sections.empty(), extraFare);
    }

    public static Line of(final String name, final String color, final Sections sections, final Fare extraFare) {
        return new Line(name, color, sections, extraFare);
    }

    public static Line of(final String name, final String color, final Fare extraFare) {
        return new Line(name, color, extraFare);
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

    public BigDecimal extraFare() {
        return extraFare.value();
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
