package nextstep.subway.line.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

@Entity
public class Line extends BaseEntity {
    private static final Fare DEFAULT_EXTRA_FARE = Fare.ZERO;

    @Embedded
    private final Sections sections = new Sections();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private LineName name;
    @Embedded
    private LineColor color;
    @Embedded
    private Fare extraFare;

    public Line() {
    }

    public Line(String name, String color) {
        this(name, color, DEFAULT_EXTRA_FARE);
    }

    public Line(String name, String color, Fare extraFare) {
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
        this.extraFare = extraFare;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color, upStation, downStation, distance, DEFAULT_EXTRA_FARE.value());
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int extraFare) {
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
        sections.add(new Section.Builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .line(this)
                .build());
        this.extraFare = Fare.from(extraFare);
    }

    public static Line of(String name, String color) {
        return new Line(name, color);
    }

    public static Line of(String name, String color, int extraFare) {
        return new Line(name, color, Fare.from(extraFare));
    }

    public void update(Line line) {
        this.name = LineName.from(line.getName());
        this.color = LineColor.from(line.getColor());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return Objects.nonNull(name) ? name.toString() : "";
    }

    public String getColor() {
        return Objects.nonNull(color) ? color.toString() : "";
    }

    public int getExtraFare() {
        return extraFare.value();
    }

    public List<Section> getSections() {
        return sections.get();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void addLineStation(Section section) {
        sections.add(section);
    }

    public void removeLineStation(Station station) {
        sections.remove(station);
    }
}
