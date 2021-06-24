package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    public static final String EXISTS_SECTION_EXCEPTION_MESSAGE = "이미 등록된 구간 입니다.";
    public static final String NOT_EXISTS_ALL_STATIONS_EXCEPTION_MESSAGE = "등록할 수 없는 구간 입니다.";
    public static final String AT_LEAST_ONE_SECTION_EXCEPTION_MESSAGE = "구간은 최소 한개 이상이어야만 합니다.";

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections;

    protected Sections() {
        sections = new ArrayList<>();
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station firstUpStation = findFirstUpStation();
        stations.add(firstUpStation);
        addNextStation(stations, firstUpStation);
        return stations;
    }

    private void addNextStation(List<Station> stations, Station currentStation) {
        Optional<Section> nextSection = findNextSection(currentStation);
        while (nextSection.isPresent()) {
            currentStation = nextSection.get().getDownStation();
            stations.add(currentStation);
            nextSection = findNextSection(currentStation);
        }
    }

    private Optional<Section> findNextSection(Station currentStation) {
        return sections.stream()
                .filter(it -> it.isUpStationEqualsToStation(currentStation))
                .findFirst();
    }

    private Station findFirstUpStation() {
        Station currentStation = sections.get(0).getUpStation();
        Optional<Section> previousSection = findPreviousSection(currentStation);
        while (previousSection.isPresent()) {
            currentStation = previousSection.get().getUpStation();
            previousSection = findPreviousSection(currentStation);
        }
        return currentStation;
    }

    private Optional<Section> findPreviousSection(Station currentStation) {
        return sections.stream()
                .filter(it -> it.isDownStationEqualsToStation(currentStation))
                .findFirst();
    }

    public void add(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(section::isUpStationEqualsToStation);
        boolean isDownStationExisted = stations.stream().anyMatch(section::isDownStationEqualsToStation);

        if (stations.isEmpty()) {
            sections.add(section);
            return;
        }

        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException(EXISTS_SECTION_EXCEPTION_MESSAGE);
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new IllegalArgumentException(NOT_EXISTS_ALL_STATIONS_EXCEPTION_MESSAGE);
        }

        if (isUpStationExisted) {
            sections.stream()
                    .filter(section::isUpStationEqualsToUpStationInSection)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section));
        }

        if (isDownStationExisted) {
            sections.stream()
                    .filter(section::isDownStationEqualsToDownStationInSection)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section));
        }

        sections.add(section);
    }

    public void removeSection(Line line, Station station) {
        if (sections.size() <= 1) {
            throw new IllegalArgumentException(AT_LEAST_ONE_SECTION_EXCEPTION_MESSAGE);
        }

        Optional<Section> nextSection = findNextSection(station);
        Optional<Section> previousSection = findPreviousSection(station);

        if (nextSection.isPresent() && previousSection.isPresent()) {
            Station newUpStation = previousSection.get().getUpStation();
            Station newDownStation = nextSection.get().getDownStation();
            Distance newDistance = nextSection.get().getDistance().plus(previousSection.get().getDistance());
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        nextSection.ifPresent(sections::remove);
        previousSection.ifPresent(sections::remove);
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }
}
