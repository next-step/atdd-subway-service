package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.line.domain.wrappers.Sections;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

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

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.addSection(new Section(this, upStation, downStation, distance));
    }

    public Line(Long id, String name, Station upStation, Station downStation, int distance) {
        this.id = id;
        this.name = name;
        sections.addSection(new Section(this, upStation, downStation, distance));
    }

    public Line(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int extraFare) {
        this.name = name;
        this.color = color;
        sections.addSection(new Section(this, upStation, downStation, distance));
        this.extraFare = extraFare;
    }

    public void update(Line line) {
        this.name = line.name;
        this.color = line.color;
    }

    public void addSection(Section section) {
        sections.addSection(section);
        section.lineBy(this);
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

    public Sections getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return sections.stations();
    }

    public int getExtraFare() {
        return extraFare;
    }

    public void addLineStation(Section section) {
        sections.updateSections(section);
    }

    public void removeSection(Station station) {
        sections.removeSection(station, this);
    }

}
