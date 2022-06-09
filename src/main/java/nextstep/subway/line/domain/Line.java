package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineExceptionType.ALREADY_ADDED_SECTION;
import static nextstep.subway.line.domain.LineExceptionType.CANNOT_ADDED_SECTION;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

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
    private Sections sections = Sections.createEmpty();
    @Embedded
    private Fare fare;

    protected Line() {
    }

    private Line(String name, String color, int fare) {
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
        this.fare = Fare.from(fare);
    }

    private Line(String name, String color, Station upStation, Station downStation, int distance, int fare) {
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
        this.fare = Fare.from(fare);
        this.sections.add(Section.of(this, upStation, downStation, distance));
    }

    public static Line of(String name, String color, int fare) {
        return new Line(name, color, fare);
    }

    public static Line of(String name, String color, Station upStation, Station downStation, int distance, int fare) {
        return new Line(name, color, upStation, downStation, distance, fare);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public Long getId() {
        return id;
    }

    public LineName getName() {
        return this.name;
    }

    public LineColor getColor() {
        return color;
    }

    public Fare getFare() {
        return fare;
    }

    public void addSection(Section section) {
        validateAlreadyAddedSection(section);
        validateDoesntExistSection(section);
        this.sections.add(section);
        section.registerLine(this);
    }

    public void removeSection(Station station) {
        this.sections.removeStation(station);
    }

    public List<Station> getStations() {
        return this.sections.getStations();
    }

    public Sections getSections() {
        return this.sections;
    }

    private void validateDoesntExistSection(Section section) {
        if (this.sections.isEmpty()) {
            return;
        }

        if (this.sections.containUpDownStation(section)) {
            throw new IllegalStateException(CANNOT_ADDED_SECTION.getMessage());
        }
    }

    private void validateAlreadyAddedSection(Section section) {
        if (this.sections.containStationBySection(section)) {
            throw new IllegalStateException(ALREADY_ADDED_SECTION.getMessage());
        }
    }
}
