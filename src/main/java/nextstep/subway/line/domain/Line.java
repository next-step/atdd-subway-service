package nextstep.subway.line.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

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

    private int extraFare;

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color, upStation, downStation, distance, 0);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int extraFare) {
        this.name = name;
        this.color = color;
        sections.addSection(this, new Section(this, upStation, downStation, distance));
        this.extraFare = extraFare;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(final Section section) {
        sections.addSection(this, section);
    }

    public void removeStation(final Station station) {
        sections.removeStation(station);
    }

    public List<Station> computeSortedStations() {
        return sections.computeSortedStations();
    }

    public Set<Station> extractAllStations() {
        return sections.extractAllStations();
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
        return sections.getSections();
    }
}
