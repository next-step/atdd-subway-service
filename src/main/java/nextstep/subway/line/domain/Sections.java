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

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> elements = new ArrayList<>();

    public void addSection(Section section) {
        if (elements.isEmpty()) {
            elements.add(section);
            return;
        }
        List<Station> stations = getAllStation();
        validateSections(stations, section);
        elements.forEach(it -> it.update(section));
        elements.add(section);
    }

    private void validateSections(List<Station> stations, Section targetSection) {
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
            .filter(it -> it.getUpStation() == station)
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

    public List<Section> getElements() {
        return elements;
    }
}
