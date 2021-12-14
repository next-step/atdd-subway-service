package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
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
    public static final String ERROR_NONE_MATCHED_STATIONS = "등록할 수 없는 구간 입니다.";
    public static final String ERROR_ALREADY_ADDED_SECTION = "이미 등록된 구간 입니다.";

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

        findSameUpStation(section.getUpStation()).ifPresent(it -> {
            it.updateUpStationMinus(section);
            return;
        });

        findSameDownStation(section.getDownStation())
            .ifPresent(it -> it.updateDownStationMinus(section));
    }

    private Optional<Section> findSameDownStation(Station station) {
        return sections.stream()
            .filter(it -> it.isEqualToDownStation(station))
            .findAny();
    }

    private Optional<Section> findSameUpStation(Station station) {
        return sections.stream()
            .filter(it -> it.isEqualToUpStation(station))
            .findAny();
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

    private Set<Station> getStations() {
        Set<Station> stations = new HashSet<>();
        sections.forEach(it -> {
            stations.add(it.getUpStation());
            stations.add(it.getDownStation());
        });
        return stations;
    }

    public List<Section> getSections() { //오류방지
        return null;
    }

    public List<Station> getSortedStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(findAllStation());
    }

    private List<Station> findAllStation() {
        Section firstSection = findFirstSection();
        List<Station> stations = new ArrayList<>(
            Arrays.asList(firstSection.getUpStation(), firstSection.getDownStation())
        );

        List<Station> upStations = upStationsOfSections();
        Section lastSection = firstSection;
        while (upStations.contains(lastSection.getDownStation())) {
            Station finalDownStation = lastSection.getDownStation();
            lastSection = sections.stream()
                .filter(section -> section.isEqualToUpStation(finalDownStation))
                .findFirst()
                .orElseThrow(RuntimeException::new);
            stations.add(lastSection.getDownStation());
        }
        return stations;
    }

    private Section findFirstSection() {
        List<Station> downStations = downStationsOfSections();
        Section firstSection = sections.get(0);
        while (downStations.contains(firstSection.getUpStation())) {
            Station finalUpStation = firstSection.getUpStation();
            firstSection = sections.stream()
                .filter(section -> section.isEqualToDownStation(finalUpStation))
                .findFirst()
                .orElseThrow(RuntimeException::new);
        }
        return firstSection;
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
}
