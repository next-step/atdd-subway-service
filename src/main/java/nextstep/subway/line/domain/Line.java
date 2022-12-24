package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;
    @Column(nullable = false, columnDefinition = "bigint default 0")
    private long addFare;

    @Embedded
    private final Sections sections = new Sections();

    public Line() {}

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, long addFare) {
        this.name = name;
        this.color = color;
        this.addFare = addFare;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        addSection(new Section(this, upStation, downStation, distance));
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, long addFare) {
        this.name = name;
        this.color = color;
        this.addFare = addFare;
        addSection(new Section(this, upStation, downStation, distance));
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        section.belongLine(this);
        this.sections.add(section);
    }

    public List<Station> findSortedStations() {
        return this.sections.getSortedStations();
    }

    public void removeStation(Station station) {
        this.sections.removeStation(station);
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

    public List<Section> getSections() {
        return sections.asList();
    }

    public long getAddFare() { return addFare; }
}
