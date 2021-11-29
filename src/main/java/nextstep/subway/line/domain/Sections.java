package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        List<Station> stations = new ArrayList<>();
        Section section = extractFirstSection();
        stations.add(section.getUpStation());
        Set<Station> upStations = extractUpStations();
        while (!isLastStation(section.getDownStation(), upStations)) {
            Section extractSection = extractSectionByContainsUpStation(section.getDownStation());
            stations.add(extractSection.getUpStation());
            section = extractSection;
        }
        stations.add(section.getDownStation());
        return stations;
    }

    public void addSection(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }
        validateAddSection(section);
        if (!addStationOfBetween(section)) {
            sections.add(section);
        }
    }

    private boolean addStationOfBetween(Section section) {
        return addUpStationOfBetween(section) || addDownStationOfBetween(section);
    }

    private boolean isLastStation(Station downStation, Set<Station> upStations) {
        return !upStations.contains(downStation);
    }

    private Section extractSectionByContainsUpStation(Station downStation) {
        return sections.stream()
            .filter(section -> section.getUpStation().equals(downStation))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    private Section extractFirstSection() {
        Set<Station> downStations = extractDownStations();
        return sections.stream()
            .filter(section -> !downStations.contains(section.getUpStation()))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    private Set<Station> extractAllStations() {
        Set<Station> allStations = extractUpStations();
        allStations.addAll(extractDownStations());
        return allStations;
    }

    private Set<Station> extractUpStations() {
        return sections.stream()
            .map(Section::getUpStation)
            .collect(Collectors.toSet());
    }

    private Set<Station> extractDownStations() {
        return sections.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toSet());
    }

    private boolean addUpStationOfBetween(Section section) {
        Optional<Section> findSection = sections.stream()
            .filter(it -> it.getUpStation() == section.getUpStation())
            .findFirst();
        if (!findSection.isPresent()) {
            return false;
        }
        findSection.get().updateUpStation(section.getDownStation(), section.getDistance());
        return sections.add(section);
    }

    private boolean addDownStationOfBetween(Section section) {
        Optional<Section> findSection = sections.stream()
            .filter(it -> it.getDownStation() == section.getDownStation())
            .findFirst();
        if (!findSection.isPresent()) {
            return false;
        }
        findSection.get().updateDownStation(section.getUpStation(), section.getDistance());
        return sections.add(section);
    }

    private void validateAddSection(Section section) {
        Set<Station> allStations = extractAllStations();
        validateDuplicate(section, allStations);
        validateNonExist(section, allStations);
    }

    private void validateDuplicate(Section section, Set<Station> allStations) {
        if (allStations.contains(section.getUpStation())
            && allStations.contains(section.getDownStation())) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void validateNonExist(Section section, Set<Station> allStations) {
        if (!allStations.contains(section.getUpStation())
            && !allStations.contains(section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public void removeLineStation(Station station, Line line) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

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
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }
}
