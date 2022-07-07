package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.fare.Fare;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

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

    @Embedded
    private Fare charge = new Fare(0);

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

    public Fare getCharge() {
        return charge;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return new ArrayList<>();
        }

        Stations stations = new Stations();
        Station station = findUpStation();
        stations.add(station);

        while (sections.hasNextUpSection(station)) {
            Section nextLineStation = sections.findSectionByUpStation(station);
            station = nextLineStation.getDownStation();
            stations.add(station);
        }

        return stations.getValues();
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void removeSection(Station station) {
        this.sections.cutOff(this, station);
    }

    private Station findUpStation() {
        Station station = sections.getValues().get(0).getUpStation();

        while (sections.hasNextDownSection(station)) {
            Section section = sections.findSectionByDownStation(station);
            station = section.getUpStation();
        }

        return station;
    }
}
