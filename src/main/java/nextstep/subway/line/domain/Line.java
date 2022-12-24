package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.fare.domain.Fare;
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

    @Embedded
    private Sections sections = new Sections();

    @Embedded
    private Fare extraFare;

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.sections = new Sections(new Section(this, upStation, downStation, distance));
        this.extraFare = Fare.from(0);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, Fare extraFare) {
        this.name = name;
        this.color = color;
        this.sections = new Sections(new Section(this, upStation, downStation, distance));
        this.extraFare = extraFare;
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

    public List<Station> getStations() {
        return sections.getStations();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void removeStation(Station station) {
        sections.removeStation(station);
    }

    public Fare getExtraFare() {
        return extraFare;
    }
}
