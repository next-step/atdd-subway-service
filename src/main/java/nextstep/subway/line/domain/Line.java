package nextstep.subway.line.domain;

import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.fare.domain.Fare;
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
    private Fare fare = new Fare(0);

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation,
        int distance) {
        this(name, color, upStation, downStation, new Distance(distance));
    }

    public Line(String name, String color, Station upStation, Station downStation,
        Distance distance) {
        this.name = name;
        this.color = color;
        this.sections = Sections.of(new Section(this, upStation, downStation, distance));
    }

    public Line(String name, String color, Fare fare) {
        this(name, color, Collections.emptyList(), fare);
    }

    public Line(String name, String color, List<Section> sections, Fare fare) {
        this.name = name;
        this.color = color;
        this.sections = new Sections(sections);
        this.fare = fare;
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

    public Sections getSections() {
        return sections;
    }

    public Fare getFare() {
        return fare;
    }

    public void addSection(Station upStation, Station downStation, Distance distance) {
        sections.add(this, upStation, downStation, distance);
    }

    public void removeStation(Station station) {
        sections.remove(this, station);
    }

    public boolean contains(Section section) {
        return sections.contains(section);
    }
}
