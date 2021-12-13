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
    private Sections sections = new Sections();

    public Line() { }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        addSection(upStation, downStation, distance);
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public List<Station> getStationsInOrder() {
        return sections.getStationsInOrder();
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void removeSectionByStation(Station station) {
        sections.removeSectionByStation(station);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void update(Line newLine) {
        this.name = newLine.getName();
        this.color = newLine.getColor();
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