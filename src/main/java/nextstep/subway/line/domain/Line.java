package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;

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
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color, int fare) {
        this.name = name;
        this.color = color;
        this.fare = fare;
    }

    public Line(String name, String color, int fare, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.fare = fare;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
        this.fare = line.getFare();
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
        return sections.getStations();
    }

    public int getFare() {
        return fare;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void removeStation(Station station) {
        sections.removeStation(station);
    }
}
