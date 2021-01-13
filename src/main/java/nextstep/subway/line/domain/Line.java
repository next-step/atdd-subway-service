package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
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

    public List<Station> getStations() {
        Section startSection = findStartSection(sections.get(0));
        List<Station> stations = new ArrayList<>();
        stations.add(startSection.getUpStation());
        Station nextStation = startSection.getDownStation();
        while (nextStation != null) {
            stations.add(nextStation);
            nextStation = findDownStation(nextStation);
        }
        return stations;
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        List<Station> stations = this.getStations();
        if (stations.isEmpty()) {
            sections.add(new Section(this, upStation, downStation, distance));
            return;
        }
        if (stations.contains(upStation) == stations.contains(downStation)) {
            throw new RuntimeException();
        }
        tryAddToUp(upStation, downStation, distance);
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void removeStation(Station station) {
        checkValidation(station);
        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            sections.add(createNewSection(upLineStation.get(), downLineStation.get()));
        }
        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void checkValidation(Station station) {
        if (sections.size() <= 1 || !getStations().contains(station)) {
            throw new RuntimeException();
        }
    }

    private Station findDownStation(Station station) {
        return this.sections.stream()
                .filter(it -> it.getUpStation().equals(station))
                .findFirst()
                .map(Section::getDownStation)
                .orElse(null);
    }

    private Section findStartSection(Section section) {
        Optional<Section> optionalSection = sections.stream()
                .filter(it -> it.getDownStation().equals(section.getUpStation()))
                .findFirst();
        if (!optionalSection.isPresent()) {
            return section;
        }
        return findStartSection(optionalSection.get());
    }

    private void tryAddToUp(Station upStation, Station downStation, int distance) {
        Optional<Section> section = sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst();
        if (section.isPresent()) {
            section.get().updateUpStation(downStation, distance);
            return;
        }
        tryAddToDown(upStation, downStation, distance);
    }

    private void tryAddToDown(Station upStation, Station downStation, int distance) {
        Optional<Section> section = sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst();
        section.ifPresent(value -> value.updateDownStation(upStation, distance));
    }

    private Section createNewSection(Section upLineSection, Section downLineSection) {
        Station newUpStation = downLineSection.getUpStation();
        Station newDownStation = upLineSection.getDownStation();
        int newDistance = upLineSection.getDistance() + downLineSection.getDistance();
        return new Section(this, newUpStation, newDownStation, newDistance);
    }

}
