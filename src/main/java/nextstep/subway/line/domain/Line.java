package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
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
    private int additionalFare;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.additionalFare = 0;
        this.sections = new Sections();
    }

    public static Line of(String name, String color, Station upStation, Station downStation, Distance distance, int additionalFare) {
        Line line = new Line(name, color);
        line.addFare(additionalFare);
        line.addSection(Section.of(line, upStation, downStation, distance));
        return line;
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

    public Sections getSections() {
        return sections;
    }

    public int getAdditionalFare() {
        return additionalFare;
    }

    public void addSection(Section section) {
        this.sections.addSection(section);
    }

    public void remove(Station station) {
        this.sections.remove(station);
    }

    public List<Station> findAllStations() {
        return this.sections.findOrderedAllStations();
    }

    private void addFare(int additionalFare) {
        this.additionalFare = additionalFare;
    }
}
