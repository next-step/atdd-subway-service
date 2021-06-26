package nextstep.subway.line.domain;

import java.util.List;

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

    private int addFare = 0;

    @Embedded
    private Sections sections = new Sections();

    public Line() {}

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color, Station upStation, Station downStation, int distance) {
        this(id, name, color, upStation, downStation, distance, 0);
    }

    public Line(Long id, String name, String color, Station upStation, Station downStation, int distance, int addFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.addFare = addFare;
        this.sections = Sections.of(new Section(this, upStation, downStation, distance));
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color, upStation, downStation, distance, 0);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int addFare) {
        this.name = name;
        this.color = color;
        this.addFare = addFare;
        this.sections = Sections.of(new Section(this, upStation, downStation, distance));
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

    public int getAddFare() {
        return addFare;
    }

    public void removeStation(Station station) {
        this.sections.removeStation(station, this);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void addSection(Section section) {
        sections.addSection(section);
    }

    public int getDistanceBetweenStations(Station upStation, Station downStation) {
        return sections.getDistanceBetweenStations(upStation, downStation);
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

}
