package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.sections.addSection(Section.of(this, upStation, downStation, distance));
    }

    public static Line of() {
        return new Line();
    }

    public static Line of(String name, String color, Station upStation, Station downStation, int distance) {
        Line line = new Line(name, color);
        Section section = Section.of(line, upStation, downStation, distance);
        line.sections.addSection(section);
        return line;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public List<Station> getStations() {
        return this.sections.getStations();
    }

    public void addSection(Section section) {
        this.sections.addSection(section);
    }

    public void remove(Station station) {
        this.sections.remove(this, station);
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
}
