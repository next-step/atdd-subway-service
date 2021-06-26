package nextstep.subway.line.domain;

import nextstep.subway.exception.CannotAddException;
import nextstep.subway.exception.CannotDeleteException;
import nextstep.subway.exception.DataAlreadyExistsException;
import nextstep.subway.exception.Message;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.function.Predicate;

@Embeddable
public class Sections {

    private static final int MINIMUM_REMOVABLE_SIZE = 2;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new LinkedList<>();

    public Sections() {
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getSortedStation() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }
        List<Station> stations = new ArrayList<>();
        Station firstStation = newFindUpStation();
        stations.add(firstStation);

        sortStationInOrder(stations, firstStation);
        return stations;
    }

    private void sortStationInOrder(List<Station> stations, Station station) {
        Station startStation = station;

        Section nextLineStation = findSectionByFilter(section -> section.hasUpStationSameWith(startStation));
        if (nextLineStation != null) {
            station = nextLineStation.getDownStation();
            stations.add(station);
            sortStationInOrder(stations, station);
        }
    }

    private Station newFindUpStation() {
        Station expectedFirstStationOfLine = sections.get(0).getUpStation();
        while (expectedFirstStationOfLine != null) {
            Station assumedFirstStation = expectedFirstStationOfLine;
            Section nextSection = findSectionByFilter(section -> section.hasDownStationSameWith(assumedFirstStation));
            if (nextSection == null) {
                break;
            }
            expectedFirstStationOfLine = nextSection.getUpStation();
        }

        return expectedFirstStationOfLine;
    }

    public void add(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        List<Station> stations = getSortedStation();
        boolean isUpStationExisted = isIncludingStation(stations, section.getUpStation());
        boolean isDownStationExisted = isIncludingStation(stations, section.getDownStation());

        validateSectionBeforeAdd(isUpStationExisted, isDownStationExisted);
        if (isUpStationExisted) {
            addSectionWhenUpStationExists(section);
        }

        if (isDownStationExisted) {
            addSectionWhenDownStationExists(section);
        }
    }

    private boolean isIncludingStation(List<Station> stations, Station station){
        return station.isIncludedIn(stations);
    }

    private void validateSectionBeforeAdd(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new DataAlreadyExistsException(Message.ERROR_SECTION_ALREADY_EXISTS);
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new CannotAddException(Message.ERROR_ONE_OF_STATION_SHOULD_BE_REGISTERED);
        }
    }

    private void addSectionWhenDownStationExists(Section section) {
        Section hasSameDownStation = findSectionByFilter(it -> it.hasDownStationSameWith(section.getDownStation()));
        if (hasSameDownStation != null) {
            hasSameDownStation.updateDownStation(section.getUpStation(), section.getDistance());
        }
        sections.add(section);
    }

    private void addSectionWhenUpStationExists(Section section) {
        Section hasSameUpStation = findSectionByFilter(it -> it.hasUpStationSameWith(section.getUpStation()));
        if (hasSameUpStation != null) {
            hasSameUpStation.updateUpStation(section.getDownStation(), section.getDistance());
        }
        sections.add(section);
    }

    public boolean isRemovableStatus() {
        return sections.size() >= MINIMUM_REMOVABLE_SIZE;
    }

    public void removeStation(Station station) {
        if (!isRemovableStatus()) {
            throw new CannotDeleteException(Message.ERROR_SECTIONS_SIZE_TOO_SMALL_TO_DELETE);
        }

        Section sectionContainingUpStation = findSectionByFilter(it -> it.hasUpStationSameWith(station));
        Section sectionContainingDownStation = findSectionByFilter(it -> it.hasDownStationSameWith(station));

        if (sectionContainingUpStation != null && sectionContainingDownStation != null) {
            removeMiddleStationOf(sectionContainingDownStation, sectionContainingUpStation);
            return;
        }

        if (sectionContainingUpStation == null && sectionContainingDownStation != null) {
            sections.remove(sectionContainingDownStation);
            return;
        }

        if (sectionContainingUpStation != null) {
            sections.remove(sectionContainingUpStation);
        }
    }

    private void removeMiddleStationOf(Section upSection, Section downSection) {
        downSection.removeConnectionWith(upSection);
        sections.remove(upSection);
    }

    private Section findSectionByFilter(Predicate<Section> predicate){
        return getSections().stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }
}
