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

    @Embedded
    private LineName name;

    @Embedded
    private LineColor color;

    @Embedded
    private final Sections sections = Sections.createEmpty();

    protected Line() {}

    private Line(Long id) {
        this.id = id;
    }

    private Line(String name, String color) {
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
    }

    private Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
        sections.add(Section.of(this, upStation, downStation, Distance.from(distance)));
    }

    public static Line createEmpty() {
        return new Line();
    }

    public static Line from(long id) {
        return new Line(id);
    }

    public static Line of(String name, String color) {
        return new Line(name, color);
    }

    public static Line of(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void removeStation(Station station) {
        sections.removeStation(station);
    }

    public List<Station> getStations() {
        return sections.getStations();
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
}
