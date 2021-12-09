package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private LineName name;

    @Embedded
    private LineColor color;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = new LineName(name);
        this.color = new LineColor(color);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color);
        addToSections(upStation, downStation, distance);
    }

    public void update(Line line) {
        this.name = line.getLineName();
        this.color = line.getLineColor();
    }

    public Long getId() {
        return id;
    }

    public LineName getLineName() {
        return name;
    }

    public LineColor getLineColor() {
        return color;
    }

    public String getName() {
        return name.getName();
    }

    public String getColor() {
        return color.getColor();
    }

    private void addToSections(Station upStation, Station downStation, int distance) {
        sections.addToSections(this, upStation, downStation, new Distance(distance));
    }

    public void addLineStation(Station upStation, Station downStation, int distance) {
        sections.addLineStation(this, upStation, downStation, new Distance(distance));
    }

    public void removeLineStation(Station station) {
        sections.removeLineStation(this, station);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(name, line.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
