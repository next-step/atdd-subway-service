package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    private static final String ERROR_NONE_MATCHED_STATIONS = "등록할 수 없는 구간 입니다.";
    private static final String ERROR_ALREADY_ADDED_SECTION = "이미 등록된 구간 입니다.";
    private static final String ERROR_CANNOT_DELETE = "더 이상 삭제할 수 없습니다.";
    private static final String ERROR_NOT_CONTAINS_STATION = "해당 역은 포함되어 있지 않습니다.";
    private static final int SECTIONS_MIN_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public void add(Section section) {
        if (!sections.isEmpty()) {
            updateSection(section);
        }
        sections.add(section);
    }

    private void updateSection(Section section) {
        checkStationOfSection(section);

        if (updatedOfUpStationToMinus(section)) {
            return;
        }
        updatedOfDownStationToMinus(section);
    }

    private boolean updatedOfDownStationToMinus(Section section) {
        Section foundSection = findSameDownStationOfSection(section.getDownStation());
        if (!foundSection.isDummy()) {
            foundSection.updateDownStationMinus(section);
            return true;
        }
        return false;
    }

    private boolean updatedOfUpStationToMinus(Section section) {
        Section foundSection = findSameUpStationOfSection(section.getUpStation());
        if (!foundSection.isDummy()) {
            foundSection.updateUpStationMinus(section);
            return true;
        }
        return false;
    }

    public void remove(Station station) {
        checkRemoveStation(station);

        Section downStationOfSection = findSameUpStationOfSection(station);
        if (!downStationOfSection.isDummy()) {
            updatedOfDownStationToPlus(downStationOfSection);
            sections.remove(downStationOfSection);
            return;
        }

        Section upStationOfSection = findSameDownStationOfSection(station);
        if (upStationOfSection.isDummy()) {
            updatedOfUpStationToPlus(upStationOfSection);
        }
        sections.remove(upStationOfSection);
    }

    private boolean updatedOfUpStationToPlus(Section section) {
        Section foundSection = findSameUpStationOfSection(section.getDownStation());
        if (!foundSection.isDummy()) {
            foundSection.updateUpStationPlus(section);
            return true;
        }
        return false;
    }

    private boolean updatedOfDownStationToPlus(Section section) {
        Section foundSection = findSameDownStationOfSection(section.getUpStation());
        if (!foundSection.isDummy()) {
            foundSection.updateDownStationPlus(section);
            return true;
        }
        return false;
    }

    private void checkRemoveStation(Station station) {
        if (isLastSection()) {
            throw new IllegalArgumentException(ERROR_CANNOT_DELETE);
        }

        if (isNotFoundInSection(station)) {
            throw new IllegalArgumentException(ERROR_NOT_CONTAINS_STATION);
        }
    }

    private boolean isNotFoundInSection(Station station) {
        return !getStations().contains(station);
    }

    private boolean isLastSection() {
        return sections.size() <= SECTIONS_MIN_SIZE;
    }

    private Section findSameDownStationOfSection(Station station) {
        return sections.stream()
            .filter(it -> it.isEqualToDownStation(station))
            .findAny()
            .orElse(Section.DUMMY_SECTION);
    }

    private Section findSameUpStationOfSection(Station station) {
        return sections.stream()
            .filter(it -> it.isEqualToUpStation(station))
            .findAny()
            .orElse(Section.DUMMY_SECTION);
    }

    private void checkStationOfSection(Section section) {
        if (isNoneMatchStation(section)) {
            throw new IllegalArgumentException(ERROR_NONE_MATCHED_STATIONS);
        }
        if (isAlreadyAdded(section)) {
            throw new IllegalArgumentException(ERROR_ALREADY_ADDED_SECTION);
        }
    }

    private boolean isAlreadyAdded(Section section) {
        Set<Station> stations = getStations();
        return stations.contains(section.getUpStation())
            && stations.contains(section.getDownStation());
    }

    private boolean isNoneMatchStation(Section section) {
        Set<Station> stations = getStations();
        return !stations.contains(section.getUpStation())
            && !stations.contains(section.getDownStation());
    }

    public Set<Station> getStations() {
        Set<Station> stations = new HashSet<>();
        sections.forEach(it -> {
            stations.add(it.getUpStation());
            stations.add(it.getDownStation());
        });
        return stations;
    }

    public List<Station> getSortedStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(findAllStation());
    }

    private List<Station> findAllStation() {
        Section firstSection = findFirstSection();
        List<Station> stations = new ArrayList<>();
        stations.add(firstSection.getUpStation());
        stations.add(firstSection.getDownStation());

        List<Station> upStations = upStationsOfSections();
        Section lastSection = firstSection;
        while (upStations.contains(lastSection.getDownStation())) {
            Station finalDownStation = lastSection.getDownStation();
            lastSection = findEqualToUpStation(finalDownStation);
            stations.add(lastSection.getDownStation());
        }
        return stations;
    }

    private Section findFirstSection() {
        List<Station> downStations = downStationsOfSections();
        Section firstSection = sections.get(0);
        while (downStations.contains(firstSection.getUpStation())) {
            Station finalUpStation = firstSection.getUpStation();
            firstSection = findEqualToDownStation(finalUpStation);
        }
        return firstSection;
    }

    private Section findEqualToUpStation(Station finalDownStation) {
        return sections.stream()
            .filter(section -> section.isEqualToUpStation(finalDownStation))
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }

    private Section findEqualToDownStation(Station finalUpStation) {
        return sections.stream()
            .filter(section -> section.isEqualToDownStation(finalUpStation))
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }

    private List<Station> upStationsOfSections() {
        return sections.stream()
            .map(Section::getUpStation)
            .collect(Collectors.toList());
    }

    private List<Station> downStationsOfSections() {
        return sections.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return sections;
    }
}
