package nextstep.subway.line.domain;

import java.util.Objects;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
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
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance) {
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
        if (sections.isEmpty()) {
            return new Stations();
        }
        Stations stations = new Stations();
        Station downStation = sections.findFinalUpStation();
        while (downStation != null) {
            stations.add(downStation);
            downStation = sections.nextStationOf(downStation, StationPosition.DOWN_STATION);
        }
        return stations;
    }

    public void checkLineStationRemovable() {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }
    }

    public void removeLineStation(Station station) {
        Section upStationMatchSection = sections.getMatchSectionByPosition(station, StationPosition.UP_STATION);
        Section downStationMatchSection = sections.getMatchSectionByPosition(station, StationPosition.DOWN_STATION);
        sections.remove(upStationMatchSection);
        sections.remove(downStationMatchSection);
        reAssignSection(upStationMatchSection, downStationMatchSection);
    }

    private void reAssignSection(Section upStationMatchSection, Section downStationMatchSection) {
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
