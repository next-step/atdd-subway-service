package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Optional;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LineName name;
    private LineColor color;

    private Sections sections = new Sections();

    protected Line() {
    }

    public Line(String name, String color) {
        this(new LineName(name), new LineColor(color));
    }

    public Line(String name, String color, Section section) {
        this(name, color);

        addSection(section);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(new LineName(name), new LineColor(color));

        addSection(new Section(this, upStation, downStation, new Distance(distance)));
    }

    public Line(LineName name, LineColor color) {
        this.name = name;
        this.color = color;
    }

    public Optional<Distance> calcDistanceBetween(Station source, Station distance) {
        return sections.calcDistanceBetween(source, distance);
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
        Optional<Section> newSection = sections.removeStation(station);
        newSection.ifPresent(item -> addSection(item));
    }

    public void addSection(Section section) {
        section.changeLine(this);
        sections.add(section);
    }

    public boolean containsStationsExactly(Station ...stations) {
        return sections.containsStationsExactly(stations);
    }

    public boolean containsStation(Station station) {
        return sections.containsStation(station);
    }
}
