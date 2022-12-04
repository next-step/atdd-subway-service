package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.fare.Fare;
import nextstep.subway.station.domain.Station;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    public static final int ONE = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private Sections sections = new Sections();

    @Embedded
    private Fare fare = Fare.ZERO;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Fare fare) {
        this.name = name;
        this.color = color;
        this.fare = fare;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color, upStation, downStation, distance, Fare.ZERO);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, Fare fare) {
        this.name = name;
        this.color = color;
        sections.addSection(new Section(this, upStation, downStation, distance));
        this.fare = fare;
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

    public Fare getFare() {
        return fare;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void addSection(Section section) {
        sections.addSection(section);
    }

    public void removeSection(Station station) {
        sections.removeSection(station);
    }

    public void addPath(PathFinder pathFinder) {
        sections.addPath(pathFinder);
    }
}
