package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    private static final Integer MIN_LINE_STATION_SIZE = 1;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "line", cascade = {CascadeType.PERSIST,
        CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public static Sections of() {
        return new Sections();
    }

    public void add(Section section) {
        validateDuplicate(section);
        validateAddAblePosition(section);
        relocationUpStationIfSameUpStation(section);

        sections.add(section);
    }

    public List<Station> getStationsInOrder() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station upStation = findTopUpStation();
        return mapStations(upStation, stations);
    }

    public void remove(Station station) {
        validateDeleteAbleSize();

        Optional<Section> downSection = sections.stream()
            .filter(section -> section.isSameUpStation(station))
            .findFirst();
        Optional<Section> upSection = sections.stream()
            .filter(section -> section.isSameDownStation(station))
            .findFirst();

        if (upSection.isPresent() && downSection.isPresent()) {
            addSectionOfMerge(upSection.get(), downSection.get());
        }

        upSection.ifPresent(sections::remove);
        downSection.ifPresent(sections::remove);
    }

    private void addSectionOfMerge(Section upSection, Section downSection) {
        sections.add(upSection.newOfMerge(downSection));
    }

    private Station findTopUpStation() {
        Station firstUpStation = getSectionsFirstUpStation();
        return findTopUpStation(firstUpStation);
    }

    private Station findTopUpStation(Station station) {
        Station finalUpStation = station;
        Optional<Section> nextLineStation = sections.stream()
            .filter(section -> section.getDownStation() == station)
            .findFirst();

        if (nextLineStation.isPresent()) {
            finalUpStation = findTopUpStation(nextLineStation.get().getUpStation());
        }

        return finalUpStation;
    }

    private List<Station> mapStations(Station station, List<Station> stations) {
        stations.add(station);
        Optional<Section> nextSection = findNextStation(station);

        return nextSection.map(section -> mapStations(section.getDownStation(), stations))
            .orElse(stations);
    }

    private void relocationUpStationIfSameUpStation(Section newSection) {
        sections.stream()
            .filter(section -> section.isSameUpStation(newSection.getUpStation()))
            .findFirst()
            .ifPresent(section -> section.relocationUpStation(newSection));
    }

    private Optional<Section> findNextStation(Station station) {
        return sections.stream()
            .filter(section -> section.isSameUpStation(station))
            .findFirst();
    }

    private boolean isAddAblePosition(Section section) {
        List<Station> stations = getStationsInOrder();
        return !sections.isEmpty()
            && stations.stream().noneMatch(section::isSameUpStation)
            && stations.stream().noneMatch(section::isSameDownStation);
    }

    private boolean isDuplicatedSection(Section section) {
        return sections.stream()
            .anyMatch(section::isSameUpStationAndDownStation);
    }

    private void validateDuplicate(Section section) {
        if (isDuplicatedSection(section)) {
            throw InvalidParameterException.SECTION_EXIST_EXCEPTION;
        }
    }

    private void validateAddAblePosition(Section section) {
        if (isAddAblePosition(section)) {
            throw InvalidParameterException.SECTION_ADD_NO_POSITION_EXCEPTION;
        }
    }

    private void validateDeleteAbleSize() {
        if (sections.size() == MIN_LINE_STATION_SIZE) {
            throw InvalidParameterException.SECTION_ONE_COUNT_CAN_NOT_REMOVE_EXCEPTION;
        }
    }

    private Station getSectionsFirstUpStation() {
        return sections.get(0).getUpStation();
    }
}
