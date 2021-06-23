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
        Station downStation = newFindUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Section nextLineStation = findSectionWhichUpStationIs(finalDownStation);
            if (nextLineStation == null) {
                break;
            }
            downStation = nextLineStation.getDownStation();
            stations.add(downStation);
        }
        return stations;
    }

    private Station newFindUpStation() {
        Station expectedFirstStationOfLine = sections.get(0).getUpStation();
        while (expectedFirstStationOfLine != null) {
            Station assumedFirstStation = expectedFirstStationOfLine;
            Section nextSection = findSectionWhichDownStationIs(assumedFirstStation);
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
        boolean isUpStationExisted = validateUpStation(stations, section.getUpStation());
        boolean isDownStationExisted = validateDownStation(stations, section.getDownStation());

        validateSectionBeforeAdd(isUpStationExisted, isDownStationExisted);
        if (isUpStationExisted) {
            addSectionWhenUpStationExists(section);
        }

        if (isDownStationExisted) {
            addSectionWhenDownStationExists(section);
        }
    }

    private boolean validateDownStation(List<Station> stations, Station downStation) {
        return downStation.isIncludedIn(stations);
    }

    private boolean validateUpStation(List<Station> stations, Station upStation) {
        return upStation.isIncludedIn(stations);
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
        Section hasSameDownStation = findSectionWhichDownStationIs(section.getDownStation());
        if (hasSameDownStation != null) {
            hasSameDownStation.updateDownStation(section.getUpStation(), section.getDistance());
        }
        sections.add(section);
    }

    private void addSectionWhenUpStationExists(Section section) {
        Section hasSameUpStation = findSectionWhichUpStationIs(section.getUpStation());
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
        Section inputStationIsUpStation = findSectionWhichUpStationIs(station);
        Section inputStationIsDownStation = findSectionWhichDownStationIs(station);

        if (inputStationIsUpStation != null && inputStationIsDownStation != null) {
            removeMiddleStationOf(inputStationIsDownStation, inputStationIsUpStation);
            return;
        }

        if (inputStationIsUpStation == null && inputStationIsDownStation != null) {
            sections.remove(inputStationIsDownStation);
            return;
        }

        if (inputStationIsUpStation != null) {
            sections.remove(inputStationIsUpStation);
        }
    }

    private Section findSectionWhichDownStationIs(Station station) {
        return getSections().stream()
                .filter(it -> it.hasDownStationSameWith(station))
                .findFirst()
                .orElse(null);
    }

    private Section findSectionWhichUpStationIs(Station station) {
        return getSections().stream()
                .filter(it -> it.hasUpStationSameWith(station))
                .findFirst()
                .orElse(null);
    }

    private void removeMiddleStationOf(Section upSection, Section downSection) {
        downSection.removeConnectionWith(upSection);
        sections.remove(upSection);
    }
}
