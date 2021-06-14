package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
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

    public Stations findShortestRoute(Station source, Station target) {
        validateShortestRoute(source, target);

        return sections.getShortestRoute(source, target);
    }

    public Distance calcDistanceBetween(Station source, Station distance) {
        validateShortestRoute(source, distance);

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

    private void validateShortestRoute(Station source, Station distance) {
        if (!sections.containsStationsExactly(source, distance)) {
            throw new IllegalArgumentException("포함되지 않은 역이 있습니다.");
        }

        if (source == distance) {
            throw new IllegalArgumentException("같은 역끼리는 길을 찾을 수 없습니다.");
        }
    }
}
