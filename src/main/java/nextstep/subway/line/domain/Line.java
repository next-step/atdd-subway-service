package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        addToSections(upStation, downStation, distance);
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

    private boolean isEmptySections() {
        return sections.isEmpty();
    }

    private void addToSections(Station upStation, Station downStation, int distance) {
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void addLineStation(Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();
        validateAddLineStation(stations, upStation, downStation);

        if (isEmptySections()) {
            addToSections(upStation, downStation, distance);
            return;
        }

        if (isStationExisted(stations, upStation) || isStationExisted(stations, downStation)) {
            updateUpStationIfExist(upStation, downStation, distance);
            updateDownStationIfExist(upStation, downStation, distance);
            addToSections(upStation, downStation, distance);
            return;
        }

        throw new RuntimeException();
    }

    public void removeLineStation(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        updateRemovableLineStation(station);

        removeUpLineStationIfExist(station);
        removeDownLineStationIfExist(station);
    }

    private void removeUpLineStationIfExist(Station station) {
        sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst()
                .ifPresent(it -> sections.remove(it));
    }

    private void removeDownLineStationIfExist(Station station) {
        sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst()
                .ifPresent(it -> sections.remove(it));
    }

    private void updateRemovableLineStation(Station station) {
        Optional<Section> upLineStation = getRemovableUpStation(station);
        Optional<Section> downLineStation = getRemovableDownStation(station);

        if (!upLineStation.isPresent() || !downLineStation.isPresent()) {
            return;
        }

        Station newUpStation = downLineStation.get().getUpStation();
        Station newDownStation = upLineStation.get().getDownStation();
        int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
        addToSections(newUpStation, newDownStation, newDistance);
    }

    private Optional<Section> getRemovableDownStation(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private Optional<Section> getRemovableUpStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private void validateAddLineStation(List<Station> stations, Station upStation, Station downStation) {
        if (isStationExisted(stations, upStation) && isStationExisted(stations, downStation)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!isEmptySections() &&
                isNoneMatchStation(stations, upStation) &&
                isNoneMatchStation(stations, downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void updateUpStationIfExist(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void updateDownStationIfExist(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    private boolean isStationExisted(List<Station> stations, Station station) {
        return stations.stream().anyMatch(it -> it == station);
    }

    private boolean isNoneMatchStation(List<Station> stations, Station station) {
        return stations.stream().noneMatch(it -> it == station);
    }

    public List<Station> getStations() {
        if (isEmptySections()) {
            return Collections.emptyList();
        }

        Set<Station> stations = new LinkedHashSet<>();
        Section currentSection = findFirstSection();
        Section endSection = findLastSection();

        while (!currentSection.equals(endSection)) {
            addStations(stations, currentSection);
            currentSection = findNextSection(currentSection);
        }
        addStations(stations, endSection);

        return new ArrayList<>(stations);
    }

    private void addStations(Set<Station> stations, Section currentSection) {
        stations.add(currentSection.getUpStation());
        stations.add(currentSection.getDownStation());
    }

    private Section findFirstSection() {
        Set<Station> downStations = sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toCollection(HashSet::new));

        return sections.stream()
                .filter(section -> !downStations.contains(section.getUpStation()))
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    private Section findLastSection() {
        Set<Station> upStations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toCollection(HashSet::new));

        return sections.stream()
                .filter(section -> !upStations.contains(section.getDownStation()))
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    private Section findNextSection(Section currentSection) {
        return sections.stream()
                .filter(section -> section.getUpStation() == currentSection.getDownStation())
                .findAny()
                .orElseThrow(RuntimeException::new);
    }
}
