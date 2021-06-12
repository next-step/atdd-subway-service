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

    private LineName name;
    private LineColor color;

    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this(new LineName(name), new LineColor(color));
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(new LineName(name), new LineColor(color));

        sections.add(new Section(this, upStation, downStation, new Distance(distance)));
    }

    public Line(LineName name, LineColor color) {
        this.name = name;
        this.color = color;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public Long getId() {
        return id;
    }

    public LineName getName() {
        return name;
    }

    public LineColor getColor() {
        return color;
    }

    public SortedStations sortedStation() {
        return sections.toSortedStations();
    }

    public void removeStation(Station station) {
        NewSection newSection = sections.removeStation(station);
        if (newSection != null) {
            Section section = newSection.toSection(this);
            sections.add(section);
        }
    }

    public void addSection(Station upStation, Station downStation, Distance distance) {
        Section section = new Section(this, upStation, downStation, distance);
        sections.add(section);
    }
}
