package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    public static final String NOT_CONTAINS_TARGET_STATION_ERROR_MESSAGE = "삭제 대상역이 노선에 존재하지 않습니다.";
    public static int MINIMUM_REMOVE_SIZE = 1;
    public static final String CAN_NOT_REMOVE_SECTIONS_SIZE_ERROR = String
        .format("구간의 길이가 %d 이하인 경우 삭제할 수 없습니다.", MINIMUM_REMOVE_SIZE);

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> elements = new ArrayList<>();

    public void addSection(Section section) {
        if (elements.isEmpty()) {
            elements.add(section);
            return;
        }
        List<Station> stations = getAllStation();
        validateAddSection(stations, section);
        elements.forEach(it -> it.update(section));
        elements.add(section);
    }

    private void validateAddSection(List<Station> stations, Section targetSection) {
        validateHasEqualsSection(stations, targetSection);
        validateContainsAnyStation(stations, targetSection);
    }

    private void validateHasEqualsSection(List<Station> stations, Section addTargetSection) {
        if (hasStation(stations, addTargetSection.getUpStation())
            && hasStation(stations, addTargetSection.getDownStation())) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private boolean hasStation(List<Station> stations, Station station) {
        return stations.contains(station);
    }

    private void validateContainsAnyStation(List<Station> stations, Section addTargetSection) {
        if (!stations.isEmpty()
            && !hasStation(stations, addTargetSection.getUpStation())
            && !hasStation(stations, addTargetSection.getDownStation())
        ) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public List<Station> getAllStation() {
        if (elements.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> sortedByFinalUpStations = new ArrayList<>();
        sortedByFinalUpStations.add(getFinalUpStation());

        for (int i = 0; i < elements.size(); i++) {
            Station nextNode = findSectionByUpStationEqStation(sortedByFinalUpStations.get(i)).getDownStation();
            sortedByFinalUpStations.add(nextNode);
        }
        return sortedByFinalUpStations;
    }

    public Section findSectionByUpStationEqStation(Station station) {
        return elements.stream()
            .filter(it -> it.getUpStation().equals(station))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    private Station getFinalUpStation() {
        List<Station> downStations = getAllDownStations();
        return elements.stream()
            .filter(it -> !downStations.contains(it.getUpStation()))
            .map(Section::getUpStation)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    private List<Station> getAllDownStations() {
        return elements.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toList());
    }

    public void remove(Station station) {
        validateRemoveStation(station);

        Optional<Section> upLineStation = elements.stream()
            .filter(it -> it.getUpStation() == station)
            .findFirst();
        Optional<Section> downLineStation = elements.stream()
            .filter(it -> it.getDownStation() == station)
            .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            addSwapSection(downLineStation.get(), upLineStation.get());
        }

        upLineStation.ifPresent(it -> elements.remove(it));
        downLineStation.ifPresent(it -> elements.remove(it));
    }

    private void addSwapSection(Section currentSection, Section targetSection) {
        Section section = currentSection.swapDownStationToTargetDownStation(targetSection);
        elements.add(section);
    }

    private void validateRemoveStation(Station station) {
        validateSectionsSize();
        validateRemoveTargetStation(station);
    }

    private void validateSectionsSize() {
        if (elements.size() <= MINIMUM_REMOVE_SIZE) {
            throw new IllegalArgumentException(CAN_NOT_REMOVE_SECTIONS_SIZE_ERROR);
        }
    }

    private void validateRemoveTargetStation(Station station) {
        if (!contains(station)) {
            throw new IllegalArgumentException(NOT_CONTAINS_TARGET_STATION_ERROR_MESSAGE);
        }
    }

    private boolean contains(Station station) {
        return getAllStation().contains(station);
    }

    public List<Section> getElements() {
        return elements;
    }
}
