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
    private static final String INVALID_ADDABLE_SECTION = "등록할 수 없는 구간 입니다.";
    private static final String ALREADY_HAS_SECTION = "이미 등록된 구간 입니다.";
    private static final String NOT_EXIST_STATION = "존재하지 않는 지하철 역입니다.";

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

        boolean isUpStationExisted = getStations().stream().anyMatch(it -> it.equals(section.getUpStation()));
        boolean isDownStationExisted = getStations().stream().anyMatch(it -> it.equals(section.getDownStation()));

        if (isUpStationExisted) {
            findByUpStation(section.getUpStation())
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
        } else if (isDownStationExisted) {
            findByDownStation(section.getDownStation())
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
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

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        return getSortedStations();
    }

    private void removeEndStation(Station station) {
        validateNoExistStationWhenDeleteStation(station);

        if (isFirstEndStation(station)) {
            remove(findFirstSection());
            return;
        }

        remove(findLastSection());
    }

    private void removeMiddleStation(Station station) {
        Optional<Section> upLineSection = findByUpStation(station);
        Optional<Section> downLineSection = findByDownStation(station);

        if (upLineSection.isPresent() && downLineSection.isPresent()) {
            addMiddleSection(upLineSection.get(), downLineSection.get());
        }

        upLineSection.ifPresent(this::remove);
        downLineSection.ifPresent(this::remove);
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

    private Section findFirstSection() {
        List<Station> downStations = findDownStations();
        return StreamUtils.filterAndFindFirst(sections, section -> !downStations.contains(section.getUpStation()))
                          .orElseThrow(() -> new IllegalStateException(NOT_EXIST_FIRST_SECTION));
    }

    private Section findLastSection() {
        List<Station> upStations = findUpStations();
        return StreamUtils.filterAndFindFirst(sections, section -> !upStations.contains(section.getDownStation()))
                          .orElseThrow(() -> new IllegalStateException(NOT_EXIST_LAST_SECTION));
    }

    private List<Station> findUpStations() {
        return StreamUtils.mapToList(sections, Section::getUpStation);
    }

    private List<Station> findDownStations() {
        return StreamUtils.mapToList(sections, Section::getDownStation);
    }

    private List<Station> getSortedStations() {
        List<Station> stations = new ArrayList<>();
        stations.add(findFirstSection().getUpStation());

        for (int i = START_INDEX; i < sections.size(); i++) {
            Optional<Section> sectionByUpStation = findSectionByUpStation(stations.get(i));
            sectionByUpStation.map(Section::getDownStation)
                              .ifPresent(stations::add);
        }

        return stations;
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

}
