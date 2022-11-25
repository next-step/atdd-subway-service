package nextstep.subway.line.domain;

import nextstep.subway.line.exception.InvalidAddSectionException;
import nextstep.subway.line.exception.InvalidRemoveSectionException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static nextstep.subway.line.exception.InvalidAddSectionException.EXISTS_SECTION;
import static nextstep.subway.line.exception.InvalidAddSectionException.NOT_EXIST_STATIONS;
import static nextstep.subway.line.exception.InvalidRemoveSectionException.ONE_SECTION_REMAINS;

@Embeddable
public class Sections {

    private static final int ONE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }
        List<Station> stations = getStations();

        boolean isUpStationExisted = section.isUpStationExist(stations);
        boolean isDownStationExisted = section.isDownStationExist(stations);

        verifyAddSection(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            updateUpStation(section);
            sections.add(section);
            return;
        }

        updateDownStation(section);
        sections.add(section);
    }

    private void updateDownStation(Section section) {
        getDownStationSection(section)
                .ifPresent(it -> it.updateDownStation(section));
    }

    private void updateUpStation(Section section) {
        getUpStationSection(section)
                .ifPresent(it -> it.updateUpStation(section));
    }

    private void verifyAddSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new InvalidAddSectionException(EXISTS_SECTION);
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new InvalidAddSectionException(NOT_EXIST_STATIONS);
        }
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getUpStationSection(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public void removeSection(Station station) {
        if (isSectionCountLessThanOne()) {
            throw new InvalidRemoveSectionException(ONE_SECTION_REMAINS);
        }

        if (isLastStation(station)) {
            removeLastStation(station);
            return;
        }

        Section upLineStationSection = getUpStationSection(station).orElseThrow(InvalidRemoveSectionException::new);
        Section downLineStationSection = getDownStationSection(station).orElseThrow(InvalidRemoveSectionException::new);

        addSections(upLineStationSection, downLineStationSection);

        removeSections(upLineStationSection, downLineStationSection);
    }

    private void removeLastStation(Station station) {
        getUpStationSection(station).ifPresent(sections::remove);
        getDownStationSection(station).ifPresent(sections::remove);
    }

    private boolean isLastStation(Station station) {
        boolean isOnUpStation = getUpStationSection(station).isPresent();
        boolean isOnDownStation = getDownStationSection(station).isPresent();
        return (isOnUpStation && !isOnDownStation) || (!isOnUpStation && isOnDownStation);
    }

    private void removeSections(Section upLineStation, Section downLineStation) {
        sections.remove(upLineStation);
        sections.remove(downLineStation);
    }

    private void addSections(Section upLineStation, Section downLineStation) {
        Distance newDistance = upLineStation.addDistance(downLineStation);
        sections.add(
                new Section(
                        upLineStation.getLine(),
                        downLineStation.getUpStation(),
                        upLineStation.getDownStation(),
                        newDistance));
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getDownStationSection(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    private Optional<Section> getDownStationSection(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private Optional<Section> getUpStationSection(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private Optional<Section> getUpStationSection(Section section) {
        return sections.stream().filter(it -> it.isUpStation(section)).findFirst();
    }

    private Optional<Section> getDownStationSection(Section section) {
        return sections.stream().filter(it -> it.isDownStation(section)).findFirst();
    }

    private boolean isSectionCountLessThanOne() {
        return sections.size() <= ONE;
    }

}
