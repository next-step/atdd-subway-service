package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void add(Section section) {
        requireNonNull(section, "section");
        List<Station> stations = getStations();
        if (!stations.isEmpty()) {
            relocate(section, stations);
        }
        sections.add(section);
    }

    private void relocate(Section section, List<Station> stations) {
        boolean isUpStationExisted = stations.stream().anyMatch(section::matchesUpStation);
        boolean isDownStationExisted = stations.stream().anyMatch(section::matchesDownStation);
        validateExists(isUpStationExisted, isDownStationExisted);
        validateNoneMatch(section, stations);
        if (isUpStationExisted) {
            relocateForUpStation(section);
        }
        if (isDownStationExisted) {
            relocateForDownStation(section);
        }
    }

    private void validateNoneMatch(Section section, List<Station> stations) {
        if (noneMatch(section, stations)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void validateExists(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private boolean noneMatch(Section section, List<Station> stations) {
        return !stations.isEmpty() &&
                stations.stream().noneMatch(section::matchesUpStation) &&
                stations.stream().noneMatch(section::matchesDownStation);
    }

    private void relocateForDownStation(Section other) {
        findOneMatchedDownStation(other.getDownStation())
            .ifPresent(it -> it.updateDownStation(other.getUpStation(), other.getDistance()));
    }

    private Optional<Section> findOneMatchedDownStation(Station downStation) {
        return sections.stream()
                       .filter(it -> it.matchesDownStation(downStation))
                       .findFirst();
    }

    private void relocateForUpStation(Section other) {
        findOneMatchedUpStation(other.getUpStation())
            .ifPresent(it -> it.updateUpStation(other.getDownStation(), other.getDistance()));
    }

    private Optional<Section> findOneMatchedUpStation(Station upStation) {
        return sections.stream()
                       .filter(it -> it.matchesUpStation(upStation))
                       .findFirst();
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        return getStationInOrder();
    }

    private List<Station> getStationInOrder() {
        Map<Station, Station> map = sections.stream()
                                            .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
        List<Station> stations = new ArrayList<>();
        Station station = findUpStation();
        while (map.get(station) != null) {
            stations.add(station);
            station = map.get(station);
        }
        stations.add(station);
        return stations;
    }

    private Station findUpStation() {
        Set<Station> upStations = findUpStationOfSection();
        Set<Station> downStations = findDownStationOfSection();
        return upStations.stream()
                         .filter(upStation -> !downStations.contains(upStation))
                         .findFirst()
                         .orElseThrow(IllegalArgumentException::new);
    }

    private Set<Station> findUpStationOfSection() {
        return sections.stream()
                       .map(Section::getUpStation)
                       .collect(Collectors.toSet());
    }

    private Set<Station> findDownStationOfSection() {
        return sections.stream()
                       .map(Section::getDownStation)
                       .collect(Collectors.toSet());
    }

    public void removeStation(Line line, Station station) {
        validateSectionSize();
        Optional<Section> upLineStation = findOneMatchedUpStation(station);
        Optional<Section> downLineStation = findOneMatchedDownStation(station);
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            sections.add(mergeSection(line, upLineStation.get(), downLineStation.get()));
        }
        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void validateSectionSize() {
        if (sections.size() <= 1) {
            throw new RuntimeException("더 이상 역을 제거할 수 없습니다.");
        }
    }

    private Section mergeSection(Line line, Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        Distance sumDistance = Distance.sum(upLineStation.getDistance(), downLineStation.getDistance());
        return new Section(line, newUpStation, newDownStation, sumDistance);
    }
}
