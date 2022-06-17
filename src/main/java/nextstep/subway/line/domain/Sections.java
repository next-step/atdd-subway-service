package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    private static final String NONE_MATCH_STATION = "등록할 수 없는 구간 입니다.";
    private static final String STATION_EXISTED = "이미 등록된 구간 입니다.";
    private static final String NOT_REMOVED_ONLY_ONE_SECTION = "구간이 하나인 노선에서 역을 제거할 수 없습니다.";
    private static final String DOES_NOT_CONTAIN_STATION = "노선에 등록되어 있지 않은 역은 제거할 수 없습니다.";
    private static final String NOT_FOUND_FIRST_UP_SECTION = "상행 종점을 찾을 수 없습니다.";
    private static final String NOT_FOUND_NEXT_STATION = "다음 역을 찾을 수 없습니다.";

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {}

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections empty() {
        return new Sections(new ArrayList<>());
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    public List<Section> get() {
        return this.sections;
    }

    public void add(Section addedSection) {
        if (this.sections.isEmpty()) {
            this.sections.add(addedSection);
            return;
        }
        validateStationExisted(addedSection);
        validateNoneMatchStation(addedSection);

        this.sections.forEach(section -> section.updateSection(addedSection));
        this.sections.add(addedSection);
    }

    public void removeStation(Station removeStation) {
        if (removableStation(removeStation)) {
            Optional<Section> upLineStation = findSectionByDownStation(removeStation);
            Optional<Section> downLineStation = findSectionByUpStation(removeStation);

            addConnectSection(upLineStation, downLineStation);

            upLineStation.ifPresent(it -> this.sections.remove(it));
            downLineStation.ifPresent(it -> this.sections.remove(it));
        }
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        stations.add(findFirstUpSection().getUpStation());
        for (int i = 0; i < this.sections.size(); i++) {
            Station nextStation = findSectionByUpStation(stations.get(i))
                    .map(Section::getDownStation)
                    .orElseThrow(() -> new RuntimeException(NOT_FOUND_NEXT_STATION));
            stations.add(nextStation);
        }

        return stations;
    }

    private Section findFirstUpSection() {
        List<Station> downStations = this.sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
        return this.sections.stream()
                .filter(section -> !downStations.contains(section.getUpStation()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(NOT_FOUND_FIRST_UP_SECTION));
    }

    private void validateNoneMatchStation(Section section) {
        if (isNoneMatchStation(section)) {
            throw new RuntimeException(NONE_MATCH_STATION);
        }
    }

    private boolean isNoneMatchStation(Section section) {
        return doesNotContainStation(section.getUpStation()) && doesNotContainStation(section.getDownStation());
    }

    private boolean doesNotContainStation(Station station) {
        List<Station> stations = getStations();
        return stations.stream().noneMatch(it -> it == station);
    }

    private void validateStationExisted(Section section) {
        if (isStationExisted(section)) {
            throw new RuntimeException(STATION_EXISTED);
        }
    }

    private boolean isStationExisted(Section section) {
        return containsStation(section.getUpStation()) && containsStation(section.getDownStation());
    }

    private boolean containsStation(Station station) {
        List<Station> stations = getStations();
        return stations.stream().anyMatch(it -> it == station);
    }

    private boolean removableStation(Station removeStation) {
        if (onlyOneSection()) {
            throw new RuntimeException(NOT_REMOVED_ONLY_ONE_SECTION);
        }
        if (doesNotContainStation(removeStation)) {
            throw new RuntimeException(DOES_NOT_CONTAIN_STATION);
        }
        return isNotEmpty();
    }

    private boolean onlyOneSection() {
        return this.sections.size() == 1;
    }

    private boolean isNotEmpty() {
        return !this.sections.isEmpty();
    }

    private Optional<Section> findSectionByUpStation(Station upStation) {
        return this.sections.stream()
                .filter(section -> section.isEqualsUpStation(upStation))
                .findFirst();
    }

    private Optional<Section> findSectionByDownStation(Station downStation) {
        return this.sections.stream()
                .filter(section -> section.isEqualsDownStation(downStation))
                .findFirst();
    }

    private void addConnectSection(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section upSection = upLineStation.get();
            Section downSection = downLineStation.get();
            this.sections.add(upSection.mergeSection(downSection));
        }
    }
}
