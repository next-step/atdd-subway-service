package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.StreamUtils;

@Embeddable
public class Sections {
    private static final String CAN_NOT_DELETE_STATION_WHEN_ONLY_ONE_SECTIONS_MESSAGE = "노선의 구간이 1개인 경우, 지하철 역을 삭제 할 수 없습니다.";
    private static final String CAN_NOT_DELETE_WHEN_NO_EXIST_STATION = "노선에 존재하지 않는 역입니다.";
    private static final String NOT_EXIST_FIRST_SECTION = "첫 번째 구간이 존재하지 않습니다.";
    private static final String NOT_EXIST_LAST_SECTION = "마지막 구간이 존재하지 않습니다.";
    private static final String NOT_EXIST_STATION = "존재하지 않는 지하철 역입니다.";
    private static final String NOT_EXIST_SECTION_BY_STATION = "역이 포함된 구간이 없습니다.";
    private static final String INVALID_ADDABLE_SECTION = "등록할 수 없는 구간 입니다.";
    private static final String ALREADY_HAS_SECTION = "이미 등록된 구간 입니다.";
    private static final String IS_GREATER_OR_EQUAL_DISTANCE = "새로운 구간의 길이가 기존 구간길이보다 크거나 같습니다.";

    private static final int START_INDEX = 0;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {}

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections createEmpty() {
        return new Sections(new ArrayList<>());
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    public void add(Section section) {
        validateAddableStations(section);

        if (!isEmpty() && !isEndSection(section)) {
            Section middleSection = findMiddleSection(section);
            validateAddableSectionDistance(section, middleSection);

            updateMiddleSection(section, middleSection);
        }

        sections.add(section);
    }

    public void removeStation(Station station) {
        validateHasOnlyOneSectionWhenRemoveStation();
        validateNotIncludeStation(station);

        if (isEndStation(station)) {
            removeEndStation(station);
            return;
        }

        removeMiddleStation(station);
    }

    public int size() {
        return sections.size();
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public boolean containStations(List<Station> stations) {
        return findAllStations().containsAll(stations);
    }

    public boolean hasStation(Station station) {
        return findAllStations().contains(station);
    }

    public Optional<Section> findByUpStation(Station station) {
        return StreamUtils.filterAndFindFirst(sections, section -> section.isSameUpStation(station));
    }

    public Optional<Section> findByDownStation(Station station) {
        return StreamUtils.filterAndFindFirst(sections, section -> section.isSameDownStation(station));
    }

    public List<Section> getValues() {
        return Collections.unmodifiableList(sections);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        return getSortedStations();
    }

     List<Station> getSortedStations() {
        List<Station> stations = new ArrayList<>();
        stations.add(findFirstSection().getUpStation());

        for (int i = START_INDEX; i < sections.size(); i++) {
            Optional<Section> sectionByUpStation = findSectionByUpStation(stations.get(i));
            sectionByUpStation.map(Section::getDownStation)
                              .ifPresent(stations::add);
        }

        return stations;
    }

     void removeMiddleStation(Station station) {
        Optional<Section> upLineSection = findByUpStation(station);
        Optional<Section> downLineSection = findByDownStation(station);

        if (upLineSection.isPresent() && downLineSection.isPresent()) {
            addMiddleSection(upLineSection.get(), downLineSection.get());
        }

        upLineSection.ifPresent(this::remove);
        downLineSection.ifPresent(this::remove);
    }

     void removeEndStation(Station station) {
        validateNoExistStationWhenDeleteStation(station);

        if (isFirstEndStation(station)) {
            remove(findFirstSection());
            return;
        }

        remove(findLastSection());
    }

     Section findFirstSection() {
        List<Station> downStations = findDownStations();
        return StreamUtils.filterAndFindFirst(sections, section -> !downStations.contains(section.getUpStation()))
                          .orElseThrow(() -> new IllegalStateException(NOT_EXIST_FIRST_SECTION));
    }

     Section findLastSection() {
        List<Station> upStations = findUpStations();
        return StreamUtils.filterAndFindFirst(sections, section -> !upStations.contains(section.getDownStation()))
                          .orElseThrow(() -> new IllegalStateException(NOT_EXIST_LAST_SECTION));
    }

     Section findMiddleSection(Section section) {
        return findSectionByUpStation(section.getUpStation())
            .orElseGet(() -> findSectionByDownStation(section.getDownStation())
                .orElseThrow(() -> new IllegalStateException(NOT_EXIST_SECTION_BY_STATION)));
    }

    public boolean contains(Station station) {
        return findAllStations().contains(station);
    }

    private void updateMiddleSection(Section section, Section middleSection) {
        if (middleSection.isSameUpStation(section.getUpStation())) {
            middleSection.updateUpStation(section.getDownStation(), section.getDistance());
            return;
        }

        middleSection.updateDownStation(section.getUpStation(), section.getDistance());
    }

    private void addMiddleSection(Section upLineSection, Section downLineSection) {
        Station newUpStation = downLineSection.getUpStation();
        Station newDownStation = upLineSection.getDownStation();
        Distance newDistance = Distance.merge(upLineSection.getDistance(), downLineSection.getDistance());

        sections.add(Section.of(downLineSection.getLine(), newUpStation, newDownStation, newDistance));
    }

    private void remove(Section section) {
        validateHasOnlyOneSection();
        this.sections.remove(section);
    }

    private boolean isEndSection(Section section) {
        Section firstSection = findFirstSection();
        Section lastSection = findLastSection();

        return firstSection.isSameUpStation(section.getDownStation())
            || lastSection.isSameDownStation(section.getUpStation());
    }

    private boolean isEndStation(Station station) {
        Section firstSection = findFirstSection();
        Section lastSection = findLastSection();

        return firstSection.isSameUpStation(station) || lastSection.isSameDownStation(station);
    }

    private boolean isFirstEndStation(Station station) {
        return findFirstSection().isSameUpStation(station);
    }

    private List<Station> findAllStations() {
        return StreamUtils.flatMapToList(sections, Section::getStations, Collection::stream);
    }

    private Optional<Section> findSectionByUpStation(Station station) {
        return StreamUtils.filterAndFindFirst(sections, section -> section.isSameUpStation(station));
    }

    private Optional<Section> findSectionByDownStation(Station station) {
        return StreamUtils.filterAndFindFirst(sections, section -> section.isSameDownStation(station));
    }

    private List<Station> findUpStations() {
        return StreamUtils.mapToList(sections, Section::getUpStation);
    }

    private List<Station> findDownStations() {
        return StreamUtils.mapToList(sections, Section::getDownStation);
    }

    private boolean hasAnyRetainStation(List<Station> stations) {
        return StreamUtils.anyMatch(findAllStations(), stations::contains);
    }

    private void validateAddableStations(Section section) {
        if (sections.isEmpty()) {
            return;
        }

        validateAlreadyContainStations(section);
        validateNoRetainStations(section);
    }

    private void validateAlreadyContainStations(Section section) {
        if (containStations(section.getStations())) {
            throw new IllegalArgumentException(ALREADY_HAS_SECTION);
        }
    }

    private void validateNoRetainStations(Section section) {
        if (!hasAnyRetainStation(section.getStations())) {
            throw new IllegalArgumentException(INVALID_ADDABLE_SECTION);
        }
    }
    private void validateHasOnlyOneSectionWhenRemoveStation() {
        if (size() == 1) {
            throw new IllegalArgumentException(CAN_NOT_DELETE_STATION_WHEN_ONLY_ONE_SECTIONS_MESSAGE);
        }
    }

    private void validateNotIncludeStation(Station station) {
        if (!hasStation(station)) {
            throw new IllegalArgumentException(CAN_NOT_DELETE_WHEN_NO_EXIST_STATION);
        }
    }

    private void validateHasOnlyOneSection() {
        if (sections.size() == 1) {
            throw new IllegalArgumentException(CAN_NOT_DELETE_STATION_WHEN_ONLY_ONE_SECTIONS_MESSAGE);
        }
    }
    private void validateNoExistStationWhenDeleteStation(Station station) {
        if (!findAllStations().contains(station)) {
            throw new IllegalArgumentException(NOT_EXIST_STATION);
        }
    }

    private void validateAddableSectionDistance(Section section, Section middleSection) {
        if (section.isGreaterThanOrEqualDistanceTo(middleSection)) {
            throw new IllegalArgumentException(IS_GREATER_OR_EQUAL_DISTANCE);
        }
    }
}
