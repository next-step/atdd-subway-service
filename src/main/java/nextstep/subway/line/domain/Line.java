package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.Optional;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST,
        CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

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

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station firstStation = findFirstStation();
        stations.add(firstStation);
        return addDownStations(firstStation, stations);
    }

    private List<Station> addDownStations(Station firstStation, List<Station> stations) {
        Station downStation = findDownStation(firstStation);
        if (downStation == null) {
            return stations;
        }
        stations.add(downStation);
        return addDownStations(downStation, stations);
    }

    private Station findFirstStation() {
        Station downStation = sections.get(0).getUpStation();
        while (true) {
            Station upStation = findUpStation(downStation);
            if (upStation == null) {
                break;
            }
            downStation = upStation;
        }
        return downStation;
    }

    private Station findUpStation(Station station) {
        Optional<Section> nextLineStation = sections.stream()
            .filter(it -> it.getDownStation() == station)
            .findFirst();
        return nextLineStation.map(Section::getUpStation).orElse(null);
    }

    private Station findDownStation(Station station) {
        Optional<Section> nextLineStation = sections.stream()
            .filter(it -> it.getUpStation() == station)
            .findFirst();
        return nextLineStation.map(Section::getDownStation).orElse(null);
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

    public List<Section> getSections() {
        return sections;
    }


}
