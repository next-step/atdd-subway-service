package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.exception.CannotDeleteException;
import nextstep.subway.exception.CannotRegisterException;
import nextstep.subway.exception.ExceptionType;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> items = new ArrayList<>();

    public void add(Section section) {
        if (items.isEmpty()) {
            this.items.add(section);
            return;
        }

        List<Station> stations = getOrderedStations();
        boolean isUpStationExisted = isContainsStation(section.getUpStation(), stations);
        boolean isDownStationExisted = isContainsStation(section.getDownStation(), stations);
        validateSection(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            relocateSectionsByUpStation(section);
        }

        if (isDownStationExisted) {
            relocateSectionsByDownStation(section);
        }

        this.items.add(section);
    }

    private boolean isContainsStation(Station station, List<Station> stations) {
        return stations.contains(station);
    }

    private void validateSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new CannotRegisterException(ExceptionType.IS_EXIST_BOTH_STATIONS);
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new CannotRegisterException(ExceptionType.CAN_NOT_REGISTER_SECTION);
        }
    }

    private void relocateSectionsByUpStation(Section section) {
        items.stream()
            .filter(it -> it.isEqualsUpStation(section.getUpStation()))
            .findFirst()
            .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void relocateSectionsByDownStation(Section section) {
        items.stream()
            .filter(it -> it.isEqualsDownStation(section.getDownStation()))
            .findFirst()
            .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    public List<Section> getItems() {
        return items;
    }

    public List<Station> getOrderedStations() {
        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();

        Section section = findFirstSection();
        stations.add(section.getUpStation());
        stations.add(section.getDownStation());

        findNextSections(stations, section);
        return stations;
    }

    private void findNextSections(List<Station> stations, Section section) {
        Optional<Section> optionalNextSection = findNextSection(section);

        while (optionalNextSection.isPresent()) {
            Section nextSection = optionalNextSection.get();
            stations.add(nextSection.getDownStation());
            optionalNextSection = findNextSection(nextSection);
        }
    }

    private Optional<Section> findNextSection(Section section) {
        return this.items.stream()
            .filter(item -> item.isEqualsUpStation(section.getDownStation()))
            .findAny();
    }

    private Section findFirstSection() {
        Station firstStation = findUpStation();

        return items.stream()
            .filter(item -> item.isEqualsUpStation(firstStation))
            .findAny()
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_FOUND_LINE_STATION));
    }

    private Station findUpStation() {
        List<Station> upStations = this.items.stream()
            .map(Section::getUpStation)
            .collect(Collectors.toList());

        return upStations.stream()
            .filter(station -> isNoneMatchedDownStation(items, station))
            .findAny()
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_FOUND_LINE_STATION));
    }

    private boolean isNoneMatchedDownStation(List<Section> sections, Station upStation) {
        return sections.stream()
            .noneMatch(section -> section.isEqualsDownStation(upStation));
    }

    public void removeStation(Station station) {
        validateRemovable();

        Optional<Section> upLineStation = upStationConnectedSection(station);
        Optional<Section> downLineStation = downStationConnectedSection(station);

        sectionRelocate(upLineStation, downLineStation);
        upLineStation.ifPresent(it -> items.remove(it));
        downLineStation.ifPresent(it -> items.remove(it));
    }

    private Optional<Section> upStationConnectedSection(Station station) {
        return items.stream()
            .filter(it -> it.isEqualsUpStation(station))
            .findFirst();
    }

    private Optional<Section> downStationConnectedSection(Station station) {
        return items.stream()
            .filter(it -> it.isEqualsDownStation(station))
            .findFirst();
    }

    private void sectionRelocate(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        validateExistsStation(upLineStation, downLineStation);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section initUpLineStation = upLineStation.get();
            Section initDownLineStation = downLineStation.get();

            Distance newDistance =  initUpLineStation.getDistance().plus(initDownLineStation.getDistance());
            Line line = downLineStation.get().getLine();
            items.add(new Section(line, initDownLineStation.getUpStation(), initUpLineStation.getDownStation(), newDistance.getValue()));
        }
    }

    private void validateRemovable() {
        if (items.size() <= 1) {
            throw new CannotDeleteException(ExceptionType.CAN_NOT_DELETE_LINE_STATION);
        }
    }

    private void validateExistsStation(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (!upLineStation.isPresent() && !downLineStation.isPresent()) {
            throw new CannotDeleteException(ExceptionType.NOT_FOUND_LINE_STATION);
        }
    }
}
