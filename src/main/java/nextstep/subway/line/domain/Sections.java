package nextstep.subway.line.domain;

import nextstep.subway.common.exception.AlreadyRegisteredSectionException;
import nextstep.subway.common.exception.MinimumRemovableSectionSizeException;
import nextstep.subway.common.exception.NoRegisteredStationsException;
import nextstep.subway.common.exception.NotFoundEntityException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    private static final int MINIMUM_REMOVABLE_SECTION_SIZE = 1;
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public void addToSections(Line line, Station upStation, Station downStation, Distance distance) {
        sections.add(new Section(line, upStation, downStation, distance.getDistance()));
    }

    public void addLineStation(Line line, Station upStation, Station downStation, Distance distance) {
        List<Station> stations = getStations();
        validateAddLineStation(stations, upStation, downStation);

        if (isEmpty()) {
            addToSections(line, upStation, downStation, distance);
            return;
        }

        if (isStationExisted(stations, upStation) || isStationExisted(stations, downStation)) {
            updateUpStationIfExist(upStation, downStation, distance);
            updateDownStationIfExist(upStation, downStation, distance);
            addToSections(line, upStation, downStation, distance);
            return;
        }

        throw new RuntimeException();
    }

    public void removeLineStation(Line line, Station station) {
        validateMinimumRemovableSectionSize();

        updateRemovableLineStation(line, station);

        removeUpLineStationIfExist(station);
        removeDownLineStationIfExist(station);
    }

    private void validateMinimumRemovableSectionSize() {
        if (sections.size() <= MINIMUM_REMOVABLE_SECTION_SIZE) {
            throw new MinimumRemovableSectionSizeException(sections.size());
        }
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

    private void updateRemovableLineStation(Line line, Station station) {
        Optional<Section> upLineStation = getRemovableUpStation(station);
        Optional<Section> downLineStation = getRemovableDownStation(station);

        if (!upLineStation.isPresent() || !downLineStation.isPresent()) {
            return;
        }

        Station newUpStation = downLineStation.get().getUpStation();
        Station newDownStation = upLineStation.get().getDownStation();
        Distance newDistance = upLineStation.get().getDistance().add(downLineStation.get().getDistance());
        addToSections(line, newUpStation, newDownStation, newDistance);
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
            throw new AlreadyRegisteredSectionException();
        }

        if (!isEmpty() &&
                isNoneMatchStation(stations, upStation) &&
                isNoneMatchStation(stations, downStation)) {
            throw new NoRegisteredStationsException();
        }
    }

    private void updateUpStationIfExist(Station upStation, Station downStation, Distance distance) {
        sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void updateDownStationIfExist(Station upStation, Station downStation, Distance distance) {
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
        if (isEmpty()) {
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
                .orElseThrow(NotFoundEntityException::new);
    }

    private Section findLastSection() {
        Set<Station> upStations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toCollection(HashSet::new));

        return sections.stream()
                .filter(section -> !upStations.contains(section.getDownStation()))
                .findAny()
                .orElseThrow(NotFoundEntityException::new);
    }

    private Section findNextSection(Section currentSection) {
        return sections.stream()
                .filter(section -> section.getUpStation() == currentSection.getDownStation())
                .findAny()
                .orElseThrow(NotFoundEntityException::new);
    }
}
