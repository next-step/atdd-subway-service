package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.StreamUtils;

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
    private Fare fare;

    @Embedded
    private final Sections sections = Sections.createEmpty();

    protected Line() {}

    private Line(Long id) {
        this.id = id;
    }

    private Line(String name, String color, int fare) {
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
        this.fare = Fare.from(fare);
    }

    private Line(String name, String color, int fare, Station upStation, Station downStation, int distance) {
        this(name, color, fare);
        sections.add(Section.of(this, upStation, downStation, Distance.from(distance)));
    }

    public static Line createEmpty() {
        return new Line();
    }

    public static Line from(long id) {
        return new Line(id);
    }

    public static Line of(String name, String color, int fare) {
        return new Line(name, color, fare);
    }

    public static Line of(String name, String color, int fare, Station upStation, Station downStation, int distance) {
        return new Line(name, color, fare, upStation, downStation, distance);
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void removeStation(Station station) {
        sections.removeStation(station);
    }

    public boolean hasFareSection(Sections fareSections) {
        return StreamUtils.anyMatch(fareSections.getValues(),
                                    fareSection -> StreamUtils.anyMatch(getSections().getValues(),
                                                                        section -> section.isSameUpAndDownStations(fareSection)));
    }

    public Sections getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
        this.fare = line.fare;
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

    public Fare getFare() {
        return fare;
    }
}
