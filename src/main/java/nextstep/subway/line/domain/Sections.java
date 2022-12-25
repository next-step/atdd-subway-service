package nextstep.subway.line.domain;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.station.domain.Station;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private final static int ZERO = 0;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    @ReadOnlyProperty
    private final List<Section> sections;

    public Sections() {
        sections = new ArrayList<>();
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void add(Section section) {
        checkValidation(section);
        if (isSameUpStation(section.getUpStation())) {
            updateStationWhenUpStationSame(section);
        }
        if (isSameDownStation(section.getDownStation())) {
            updateStationWhenDownStationSame(section);
        }
        sections.add(section);
    }

    public List<Station> getSortedStations() {
        if (this.sections.size() <= ZERO) {
            return new ArrayList<>();
        }
        List<Station> sortedStations = new ArrayList<>();
        Section firstSection = findFirstSection();
        firstSection.addStations(sortedStations);
        addNextStation(sortedStations, firstSection);
        return sortedStations;
    }

    public List<Section> asList() {
        return this.sections;
    }

    public void removeStation(Station deleteStation) {
        if (this.sections.size() <= ZERO) {
            throw new IllegalArgumentException(ErrorCode.CAN_NOT_DELETE_STATION_CAUSE_SECTIONS_SIZE_EXCEPTION.getErrorMessage() + sections.size());
        }
        Section sectionOfMatchedUpStation = findSectionWhenMatchUpStation(deleteStation);
        Section sectionOfMatchedDownStation = findSectionWhenMatchDownStation(deleteStation);
        this.sections.add(new Section(sections.get(ZERO).getLine()
                , sectionOfMatchedDownStation.getUpStation()
                , sectionOfMatchedUpStation.getDownStation()
                , sectionOfMatchedUpStation.getDistance() + sectionOfMatchedDownStation.getDistance()));
        this.sections.remove(sectionOfMatchedUpStation);
        this.sections.remove(sectionOfMatchedDownStation);
    }

    public List<Line> findLines(List<Station> stations) {
        return sections.stream()
                .filter(section -> stations.contains(section.getUpStation()) && stations.contains(section.getDownStation()))
                .map(Section::getLine)
                .distinct()
                .collect(Collectors.toList());
    }

    private void checkValidation(Section section) {
        checkDuplicatedBothStation(section);
        checkNoMatchSection(section);
        checkDuplicatedSection(section);
    }

    private void checkDuplicatedSection(Section section) {
        if (sections.stream().anyMatch(eachSection -> eachSection.equals(section))) {
            throw new IllegalArgumentException(ErrorCode.NO_SAME_SECTION_EXCEPTION.getErrorMessage());
        }
    }

    private void checkNoMatchSection(Section section) {
        if (isSectionsNotEmpty() && isNoMatchStation(section)) {
            throw new IllegalArgumentException(ErrorCode.NO_MATCH_STATION_EXCEPTION.getErrorMessage());
        }
    }

    private void checkDuplicatedBothStation(Section section) {
        if (isSectionsSizeZero()) {
            return;
        }
        List<Station> allStation = getSortedStations();
        if (allStation.contains(section.getUpStation()) && allStation.contains(section.getDownStation())) {
            throw new IllegalArgumentException(ErrorCode.BOTH_STATION_ALREADY_EXIST_EXCEPTION.getErrorMessage());
        }
    }

    private void addNextStation(List<Station> stations, Section previousSection) {
        Optional<Section> nextSection = findNextSection(previousSection);
        while (nextSection.isPresent()) {
            Section currentSection = nextSection.get();
            currentSection.addNextStation(stations);
            nextSection = findNextSection(currentSection);
        }
    }

    private Section findFirstSection() {
        Section firstSection = sections.get(ZERO);
        Optional<Section> previousSection = findPreviousSection(firstSection);
        while (previousSection.isPresent()) {
            firstSection = previousSection.get();
            previousSection = findPreviousSection(firstSection);
        }
        return firstSection;
    }

    private Optional<Section> findNextSection(Section previousSection) {
        return sections.stream()
                .filter(section -> section.isEqualUpStationNewSectionDownStation(previousSection))
                .findFirst();
    }

    private Optional<Section> findPreviousSection(Section currentSection) {
        return sections.stream()
                .filter(section -> section.isEqualDownStationNewSectionUpStation(currentSection))
                .findFirst();
    }

    private void updateStationWhenDownStationSame(Section section) {
        this.sections.stream()
                .filter(section::isSameDownStation)
                .findFirst()
                .ifPresent(eachStation -> eachStation.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    private void updateStationWhenUpStationSame(Section section) {
        this.sections.stream()
                .filter(section::isSameUpStation)
                .findFirst()
                .ifPresent(eachStation -> eachStation.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private boolean isNoMatchStation(Section newSection) {
        return sections.stream()
                .map(Section::toStations)
                .flatMap(Collection::stream)
                .distinct()
                .noneMatch(station -> newSection.isSameDownStation(station) ||
                        newSection.isSameUpStation(station));
    }

    private boolean isSameUpStation(Station station) {
        List<Station> stations = getSortedStations();
        return stations.stream().anyMatch(eachStation -> eachStation.equals(station));
    }

    private boolean isSameDownStation(Station station) {
        List<Station> stations = getSortedStations();
        return stations.stream().anyMatch(eachStation -> eachStation.equals(station));
    }

    private Section findSectionWhenMatchUpStation(Station station) {
        return this.sections.stream()
                .filter(eachStation -> eachStation.getUpStationId() == station.getId())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_SUCH_STATION_IN_THE_LINE_EXCEPTION.getErrorMessage()));
    }

    private Section findSectionWhenMatchDownStation(Station station) {
        return this.sections.stream()
                .filter(eachStation -> eachStation.getDownStationId() == station.getId())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_SUCH_STATION_IN_THE_LINE_EXCEPTION.getErrorMessage()));
    }

    private boolean isSectionsNotEmpty() {
        return this.sections.size() > ZERO;
    }

    private boolean isSectionsSizeZero() {
        return this.sections.size() == ZERO;
    }
}
