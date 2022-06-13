package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.*;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private final Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;

        sections.addSection(new Section(this, upStation, downStation, distance));
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
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

    public static Line of(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
    }

    public Stations getStations() {
        if (sections.getSections().isEmpty()) {
            return new Stations();
        }

        Stations stations = new Stations();
        Station station = findUpStation();
        stations.add(station);

        while (sections.isNextUpSection(station)) {
            Section nextLineStation = sections.findSectionByUpStation(station);
            station = nextLineStation.getDownStation();
            stations.add(station);
        }

        return stations;
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        Section section = Section.of(this, upStation, downStation, distance);
        sections.addSection(section);
    }

    public void removeLineStation(Station station) {
        sections.removeSectionByStation(station);
    }

    private Station findUpStation() {
        Station station = sections.getSections().get(0).getUpStation();

        while (sections.isNextDownSection(station)) {
            Section section = sections.findSectionByDownStation(station);
            station = section.getUpStation();
        }

        return station;
    }
}
