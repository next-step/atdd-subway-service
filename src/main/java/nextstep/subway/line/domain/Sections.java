package nextstep.subway.line.domain;

import nextstep.subway.constant.ErrorCode;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.NoResultException;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    public static final int ONE_SECTION = 1;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    protected Sections() {}

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Station, Station> stations = sectionsToMap();
        return sortStations(stations, findUpStation(stations));
    }

    private Map<Station, Station> sectionsToMap() {
        return sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
    }

    private Station findUpStation(Map<Station, Station> stations) {
        return stations.keySet()
                .stream()
                .filter(upStation -> !stations.containsValue(upStation))
                .findFirst()
                .orElseThrow(() -> new NoResultException("상행 종착역이 존재하지 않습니다."));
    }

    private List<Station> sortStations(Map<Station, Station> stations, Station upStation) {
        List<Station> sortedStations = new ArrayList<>();

        Station currentStation = upStation;
        while (currentStation != null) {
            sortedStations.add(currentStation);
            currentStation = stations.get(currentStation);
        }

        return sortedStations;
    }

    public void add(Section newSection) {
        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }

        validateAddition(newSection);
        sections.forEach(section -> section.update(newSection));
        sections.add(newSection);
    }

    private void validateAddition(Section section) {
        validateHasBothStations(section);
        validateHasNotBothStations(section);
    }

    private void validateHasBothStations(Section section) {
        if (new HashSet<>(getStations()).containsAll(section.getStations())) {
            throw new IllegalArgumentException(ErrorCode.BOTH_STATION_EXIST.getMessage());
        }
    }

    private void validateHasNotBothStations(Section section) {
        if (hasNotBothStations(section)) {
            throw new IllegalArgumentException(ErrorCode.BOTH_STATION_NOT_EXIST.getMessage());
        }
    }

    private boolean hasNotBothStations(Section section) {
        List<Station> stations = getStations();

        return section.getStations()
                .stream()
                .noneMatch(stations::contains);
    }

    public void delete(Station station) {
        validateDeletion(station);
        Optional<Section> prevSection = findPrevSection(station);
        Optional<Section> nextSection = findNextSection(station);

        if (isMiddleSection(prevSection, nextSection)) {
            deleteMiddleSection(prevSection.get(), nextSection.get());
            return;
        }

        deleteEndSection(prevSection, nextSection);
    }

    private void validateDeletion(Station station) {
        validateNotExistStation(station);
        validateOneSection();
    }

    private void validateNotExistStation(Station station) {
        if (!getStations().contains(station)) {
            throw new IllegalArgumentException(ErrorCode.STATION_IS_NOT_EXIST.getMessage());
        }
    }

    private void validateOneSection() {
        if (sections.size() == ONE_SECTION) {
            throw new IllegalArgumentException(ErrorCode.LAST_SECTION_CAN_NOT_DELETE.getMessage());
        }
    }

    private Optional<Section> findPrevSection(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualDownStation(station))
                .findFirst();
    }

    private Optional<Section> findNextSection(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualUpStation(station))
                .findFirst();
    }

    private boolean isMiddleSection(Optional<Section> prevSection, Optional<Section> nextSection) {
        return prevSection.isPresent() && nextSection.isPresent();
    }

    private void deleteMiddleSection(Section prevSection, Section nextSection) {
        sections.add(prevSection.merge(nextSection));
        sections.remove(prevSection);
        sections.remove(nextSection);
    }

    private void deleteEndSection(Optional<Section> prevSection, Optional<Section> nextSection) {
        prevSection.ifPresent(sections::remove);
        nextSection.ifPresent(sections::remove);
    }
}
