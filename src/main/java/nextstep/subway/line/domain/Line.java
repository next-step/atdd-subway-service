package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    public Station findUpStation() {
        Station downStation = this.getSections().findFirstUpStation();
        while (downStation != null) {
            Optional<Section> nextLineStation = this.getSections().findSectionByDownStation(downStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public Stations getStations() {
        if (this.getSections().isEmpty()) {
            return new Stations();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.getSections().findSectionByUpStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
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

        if (stations.isEmpty()) {
            this.getSections().add(new Section(this, upStation, downStation, distance));
            return;
        }

        if (stations.isMatchStation(upStation)) {
            this.getSections().findSectionByUpStation(upStation)
                .ifPresent(it -> it.updateUpStation(downStation, distance));

            this.getSections().add(new Section(this, upStation, downStation, distance));
        }

        if (stations.isMatchStation(downStation)) {
            this.getSections().findSectionByDownStation(downStation)
                .ifPresent(it -> it.updateDownStation(upStation, distance));

            this.getSections().add(new Section(this, upStation, downStation, distance));
        }
    }

    public LineResponse convertLineResponse() {
        return LineResponse.of(this, this.getStations().convert());
    }
}
