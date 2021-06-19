package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Optional;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private final Sections sections = new Sections(new ArrayList<>());

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

    public Stations getStations() {
        return Stations.of(this.getSections().getStations());
    }

    public void addSection(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        Stations stations = getStations();

        stations.validate(upStation, downStation);

        if (stations.isEmpty()) {
            this.sections.add(section);
            return;
        }

        if (stations.isContains(upStation)) {
            this.sections.changeUpStationIfFindEqualsUpStation(section);
            return;
        }

        if (stations.isContains(downStation)) {
            this.sections.changeDownStationIfFindEqualsDownStation(section);
            return;
        }

        throw new RuntimeException("Not Matched Equals Station");
    }

    public void removeLineStation(Station station) {
        this.sections.exist();

        Optional<Section> upStation = this.sections.getStationInUpStations(station);
        Optional<Section> downStation = this.sections.getStationInDownStations(station);
        if (upStation.isPresent() && downStation.isPresent()) {
            Station newUpStation = downStation.get().getUpStation();
            Station newDownStation = upStation.get().getDownStation();
            int newDistance = upStation.get().getDistance() + downStation.get().getDistance();
            Section section = new Section(this, newUpStation, newDownStation, newDistance);
            this.sections.add(section);
        }

        upStation.ifPresent(this.sections::remove);
        downStation.ifPresent(this.sections::remove);
    }
}
