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
    @Embedded
    private Sections sections;
    private int extraFare = 0;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections = new Sections(new Section(this, upStation, downStation, distance));
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int extraFare) {
        this(name, color, upStation, downStation, distance);
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

    public List<Section> getSections() {
        return sections.getValue();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int getExtraFare() {
        return extraFare;
    }

    public void addSection(Section section) {
        sections.addLineStations(section);
    }

    public void removeLineStation(Station station) {
        sections.removeLineStation(station);
    }

    public void addOverFare(int overFare) {
        this.extraFare = overFare;
    }
}
