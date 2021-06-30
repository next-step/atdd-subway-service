package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    public Line(String name, String color, Station upStation, Station downStation, int distance, int fare) {
        this.name = name;
        this.color = color;
        sections.addSection(new Section(this, upStation, downStation, distance));
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

    public List<Section> getSections() {
        return sections.sections();
    }

    public int getFare() {
        return fare;
    }

    public void addFare(int fare) {
        this.fare = fare;
    }

    public List<Station> assembleStations() {
        return sections.assembleStations();
    }

    public void addSection(Section section) {
        sections.addSection(section);
    }

    public void deleteSection(Station station) {
        sections.deleteSection(this, station);
    }
}
