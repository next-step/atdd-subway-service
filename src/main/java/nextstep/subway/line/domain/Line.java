package nextstep.subway.line.domain;

import java.util.*;

import javax.persistence.*;

import nextstep.subway.*;
import nextstep.subway.line.dto.*;
import nextstep.subway.station.domain.*;

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

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance) {
        this.name = name;
        this.color = color;
        sections.addSection(this, upStation, downStation, distance);
    }

    public static Line of(String name, String color, Station upStation, Station downStation, Distance distance) {
        return new Line(name, color, upStation, downStation, distance);
    }

    public static Line from(LineRequest lineRequest) {
        return new Line(lineRequest.getName(), lineRequest.getColor());
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

    public List<Station> stations() {
        return sections.stations();
    }

    public void removeLineStation(Station station) {
        sections.removeLineStation(this, station);
    }

    public void addSection(Station upStation, Station downStation, Distance distance) {
        sections.addSection(this, upStation, downStation, distance);
    }

    public Sections sections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Line))
            return false;
        Line line = (Line)o;
        return Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }
}
