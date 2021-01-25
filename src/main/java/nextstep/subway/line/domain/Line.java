package nextstep.subway.line.domain;

import java.util.List;
import java.util.Optional;

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
    private final Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.addSection(upStation, downStation, distance);
    }

    public void update(Line line) {
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

    public List<Section> getSections() {
        return sections.getSections();
    }

    public List<Station> getStations() {
        return this.sections.getStations();
    }

    public void addSection(final Station upStation, final Station downStation, final Integer distance) {
        this.sections.addSection(Section.Builder.aSection()
            .line(this)
            .upStation(upStation)
            .downStation(downStation)
            .distance(distance)
            .build());
    }

    public Optional<Section> findSection(final Station upStation, final Station downStation) {
        return this.sections.findSection(upStation, downStation);
    }

    public void removeStation(final Station station) {
        this.sections.removeStation(this, station);
    }
}
