package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.Section.DUMMY_SECTION;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.line.application.exception.SectionErrorCode;
import nextstep.subway.line.domain.fare.Money;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    private static final Integer MIN_LINE_STATION_SIZE = 1;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "line", cascade = {CascadeType.PERSIST,
        CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections of() {
        return new Sections();
    }

    public static Sections of(List<Section> sections) {
        return new Sections(sections);
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

        Section sameUpStationSection = findSameUpStationSection(station);
        Section sameDownStationSection = findSameDownStationSection(station);

        if (!sameUpStationSection.isDummy() && !sameDownStationSection.isDummy()) {
            sections.add(sameDownStationSection.newOfMerge(sameUpStationSection));
        }

        sections.remove(sameDownStationSection);
        sections.remove(sameUpStationSection);
    }

    public List<Section> getSections() {
        return sections;
    }

    private Station findTopUpStation() {
        Station firstUpStation = getSectionsFirstUpStation();
        return findTopUpStation(firstUpStation);
    }

    private Station findTopUpStation(Station station) {
        Station finalUpStation = station;
        Section nextLineStation = sections.stream()
            .filter(section -> section.isSameDownStation(station))
            .findFirst()
            .orElse(DUMMY_SECTION);

        if (!nextLineStation.isDummy()) {
            finalUpStation = findTopUpStation(nextLineStation.getUpStation());
        }

        return finalUpStation;
    }

    private List<Station> mapStations(Station station, List<Station> stations) {
        stations.add(station);
        Section nextSection = findSameUpStationSection(station);

        if (nextSection.isDummy()) {
            return stations;
        }

        return mapStations(nextSection.getDownStation(), stations);
    }

    private void relocationUpStationIfSameUpStation(Section newSection) {
        sections.stream()
            .filter(section -> section.isSameUpStation(newSection.getUpStation()))
            .findFirst()
            .ifPresent(section -> section.relocationUpStation(newSection));
    }

    private Section findSameUpStationSection(Station station) {
        return sections.stream()
            .filter(section -> section.isSameUpStation(station))
            .findFirst()
            .orElse(DUMMY_SECTION);
    }

    private Section findSameDownStationSection(Station station) {
        return sections.stream()
            .filter(section -> section.isSameDownStation(station))
            .findFirst()
            .orElse(DUMMY_SECTION);
    }

    private boolean isDuplicatedSection(Section section) {
        return sections.stream()
            .anyMatch(section::isSameUpStationAndDownStation);
    }

    private void validateDuplicate(Section section) {
        if (isDuplicatedSection(section)) {
            throw InvalidParameterException.of(SectionErrorCode.SECTION_EXIST);
        }
    }

    private void validateAddAblePosition(Section section) {
        if (isAddAblePositionInSections(section)) {
            throw InvalidParameterException.of(SectionErrorCode.SECTION_ADD_NO_POSITION);
        }
    }

    private void validateDeleteAbleSize() {
        if (sections.size() == MIN_LINE_STATION_SIZE) {
            throw InvalidParameterException.of(SectionErrorCode.SECTION_ONE_COUNT_CAN_NOT_REMOVE);
        }
    }

    private boolean isAddAblePositionInSections(Section section) {
        List<Station> stations = getStationsInOrder();
        return !sections.isEmpty()
            && stations.stream().noneMatch(section::isSameUpStation)
            && stations.stream().noneMatch(section::isSameDownStation);
    }

    private Station getSectionsFirstUpStation() {
        return sections.get(0).getUpStation();
    }

    public boolean isContainStation(Station station) {
        return !findSameUpStationSection(station).isDummy() || !findSameDownStationSection(
            station).isDummy();
    }

    public Money getLineMaxFare() {
        return sections.stream()
            .map(Section::getLineAdditionalFare)
            .max(Money::compareTo)
            .orElse(Money.ZERO);
    }
}
