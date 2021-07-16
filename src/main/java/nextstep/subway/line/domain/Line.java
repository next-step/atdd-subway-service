package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private int additionalFare;

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

    public Line(String name, String color, Station upStation, Station downStation, int distance, int additionalFare) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
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

    public int getAdditionalFare() {
        return additionalFare;
    }

    public Sections getSections() {
        return sections;
    }

    public Station findUpStation() {
        Station downStation = this.getSections().findFirstUpStation();
        Section section = getSections().findSectionByDownStation(downStation);

        while(section != null) {
            downStation = section.getUpStation();
            section = this.getSections().findSectionByDownStation(downStation);
        }

        return downStation;
    }

    public Stations getStations() {
        if (getSections().isEmpty()) {
            return new Stations();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        Section section = getSections().findSectionByUpStation(downStation);

        while (section != null) {
            stations.add(section.getDownStation());
            downStation = section.getDownStation();
            section = getSections().findSectionByUpStation(downStation);
        }

        return new Stations(stations);
    }

    public void removeStation(Station station) {
        this.getSections().validateRemoveSize();
        this.getSections().removeSectionByStation(station, this);
    }

    public void addStation(Station upStation, Station downStation, int distance) {
        Stations stations = getStations();
        stations.checkStation(upStation, downStation);

        Section sectionByUpStation = getSections().findSectionByUpStation(upStation);
        if (sectionByUpStation != null) {
            sectionByUpStation.updateUpStation(downStation, distance);
        }

        Section sectionByDownStation = getSections().findSectionByDownStation(downStation);
        if (sectionByDownStation != null) {
            sectionByDownStation.updateDownStation(upStation, distance);
        }

        this.getSections().add(new Section(this, upStation, downStation, distance));
    }

    public LineResponse convertLineResponse() {
        return LineResponse.of(this, this.getStations().convert());
    }
}
