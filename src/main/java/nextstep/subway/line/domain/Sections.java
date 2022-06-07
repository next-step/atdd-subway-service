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

    public static final int MINIMUM_SIZE = 1;
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void add(Section section) {
        requireNonNull(section, "section");
        List<Station> stations = getStationInOrder();
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

    private void validateExists(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new AlreadyRegisteredSectionException();
        }
    }

    private void validateNoneMatch(Section section, List<Station> stations) {
        if (noneMatch(section, stations)) {
            throw new CannotRegisterSectionException();
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
        return getStationInOrder();
    }

    private List<Station> getStationInOrder() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Station, Station> map = sections.stream()
                                            .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
        Station station = findUpStation();
        return getStationInOrder(map, station);
    }

    private List<Station> getStationInOrder(Map<Station, Station> map, Station station) {
        List<Station> stations = new ArrayList<>();
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
        Optional<Section> sectionWithUpStation = findOneMatchedUpStation(station);
        Optional<Section> sectionWithDownStation = findOneMatchedDownStation(station);
        if (sectionWithUpStation.isPresent() && sectionWithDownStation.isPresent()) {
            sections.add(Section.merge(line, sectionWithUpStation.get(), sectionWithDownStation.get()));
        }
        sectionWithUpStation.ifPresent(it -> sections.remove(it));
        sectionWithDownStation.ifPresent(it -> sections.remove(it));
    }

    private void validateSectionSize() {
        if (sections.size() <= MINIMUM_SIZE) {
            throw new InvalidSectionSizeException();
        }
    }
}
