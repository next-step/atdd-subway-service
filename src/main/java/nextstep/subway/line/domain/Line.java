package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.NoSuchElementException;

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
        sections.add(Section.of(this, upStation, downStation, distance));
    }

    public static Line of(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
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
        Stations stations = new Stations();
        if (sections.isEmpty()) {
            return stations;
        }

        Station station = findUpStation();
        stations.add(station);

        while (sections.hasNextSectionByUpStation(station)) {
            Section nextSection = sections.getNextSectionByUpStation(station);
            station = nextSection.getDownStation();
            stations.add(station);
        }
        return stations;
    }

    private Station findUpStation() {
        Station station = getFirstSection().getUpStation();

        while (sections.hasNextSectionByDownStation(station)) {
            Section nextSection = sections.getNextSectionByDownStation(station);
            station = nextSection.getUpStation();
        }
        return station;
    }

    private Section getFirstSection() {
        if (sections.isEmpty()) {
            throw new NoSuchElementException("구간 목록이 비어있습니다.");
        }
        return sections.getSections().get(0);
    }
}
