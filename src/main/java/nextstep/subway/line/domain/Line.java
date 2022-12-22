package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.line.constants.LineErrorMessages;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.constants.StationErrorMessages;
import nextstep.subway.station.domain.Stations;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    @Embedded
    private ExtraFare extraFare;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    private Line(String name, String color, ExtraFare extraFare, Station upStation, Station downStation, Distance distance) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public Line(String name, String color, Double extraFare, Station upStation, Station downStation, int distance) {
        this(name, color, new ExtraFare(extraFare), upStation, downStation, new Distance(distance));
    }

    public Line(String name, String color, double extraFare, Station upStation, Station downStation, int distance) {
        this(name, color, new ExtraFare(extraFare), upStation, downStation, new Distance(distance));
    }

    private Line(String name, String color, Station upStation, Station downStation, Distance distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color, upStation, downStation, new Distance(distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public Stations getStations() {
        return sections.getStations();
    }

    public void addSection(Section sectionToAdd) {
        Stations stations = getStations();
        sectionToAdd.validateSectionAddable(stations);
        sections.addWithValidationAndReassign(sectionToAdd);
    }

    private void checkLineStationRemovable() {
        if (sections.size() <= 1) {
            throw new RuntimeException(LineErrorMessages.LAST_LINE_STATION_CANNOT_BE_DELETED);
        }
    }

    public void removeLineStation(Station station) {
        checkLineStationRemovable();
        Section upStationMatchSection = sections.getMatchSectionByPosition(station, StationPosition.UP_STATION);
        Section downStationMatchSection = sections.getMatchSectionByPosition(station, StationPosition.DOWN_STATION);
        checkStationRemovable(upStationMatchSection, downStationMatchSection);
        sections.remove(upStationMatchSection);
        sections.remove(downStationMatchSection);
        unionSection(upStationMatchSection, downStationMatchSection);
    }

    private void checkStationRemovable(Section upStationMatchSection, Section downStationMatchSection) {
        if (upStationMatchSection == null && downStationMatchSection == null) {
            throw new IllegalArgumentException(LineErrorMessages.CANNOT_ADD_LINE_STATION_IF_BOTH_DOES_NOT_EXIST_IN_LINE);
        }
    }

    private void unionSection(Section upStationMatchSection, Section downStationMatchSection) {
        if (upStationMatchSection != null && downStationMatchSection != null) {
            Station newUpStation = downStationMatchSection.getUpStation();
            Station newDownStation = upStationMatchSection.getDownStation();
            Distance newDistance = upStationMatchSection.addDistanceOfSection(downStationMatchSection);
            sections.add(new Section(this, newUpStation, newDownStation, newDistance));
        }
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

    public ExtraFare getExtraFare() {
        return extraFare;
    }

    public Sections getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name)
                && Objects.equals(color, line.color) && Objects.equals(sections, line.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, sections);
    }
}
