package nextstep.subway.line.domain;

import java.util.Objects;
import java.util.Optional;
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

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
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
        Station downStation = findFinalUpStation();
        while (downStation != null) {
            stations.add(downStation);
            downStation = nextStationOf(downStation, StationPosition.DOWN_STATION);
        }
        return stations;
    }

    public Station findFinalUpStation() {
        Station finalUpStation = null;
        Station nextUpstation = sections.getFirstSectionDownStation();
        while (nextUpstation != null) {
            finalUpStation = nextUpstation;
            nextUpstation = nextStationOf(finalUpStation, StationPosition.UP_STATION);
        }
        return finalUpStation;
    }

    public Station nextStationOf(Station station, StationPosition stationPosition) {
        Station downStation = null;
        Section nextSection = sections.stream()
                .filter(section -> section.isStationOppositeOf(station, stationPosition))
                .findFirst()
                .orElse(null);
        if (nextSection != null) {
            downStation = nextSection.getStationByPosition(stationPosition);
        }
        return downStation;
    }

    public void checkLineStationRemovable() {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }
    }

    public void removeLineStation(Station station) {
        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(this, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
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
