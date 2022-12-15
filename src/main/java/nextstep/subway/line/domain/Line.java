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

    private int lineFare;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.lineFare = 0;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        addSection(upStation, downStation, distance);
        this.lineFare = 0;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int lineFare) {
        this(name, color, upStation, downStation, distance);
        this.lineFare = lineFare;
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

    public void addSection(Station upperStation, Station downStation, int distance) {
        this.sections.add(new Section(this, upperStation, downStation, distance));
    }

    public void removeStation(Station station) {
        this.sections.remove(station);
    }

    public List<Station> getStation() {
        return this.sections.getStations();
    }

    public int getLineFare() {
        return this.lineFare;
    }
}
