package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
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
    private int fare;
    @Embedded
    private final Sections sections = new Sections();

    protected Line() {
    }

    private Line(String name, String color, int fare) {
        this.name = name;
        this.color = color;
        this.fare = fare;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int fare) {
        this.name = name;
        this.color = color;
        this.fare = fare;
        sections.add(new Section(this, upStation, downStation, new Distance(distance)));
    }

    public static Line of(String name, String color, int fare) {
        return new Line(name, color, fare);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
        this.fare = line.getFare();
    }

    public int getFare() {
        return this.fare;
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

    public void add(Section section) {
        this.sections.add(section);
    }

    public void removeLineStation(Station station) {
        this.sections.removeLineStation(this, station);
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }
}
