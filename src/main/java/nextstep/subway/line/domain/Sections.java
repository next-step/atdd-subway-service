package nextstep.subway.line.domain;

import static nextstep.subway.line.enums.LineExceptionType.CANNOT_REMOVE_STATION_IS_NOT_EXIST;
import static nextstep.subway.line.enums.LineExceptionType.CANNOT_REMOVE_STATION_WHEN_ONLY_ONE_SECTIONS;
import static nextstep.subway.line.enums.LineExceptionType.NOT_EXIST_FIRST_SECTION;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.StreamUtils;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {}

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    public static Sections createEmpty() {
        return new Sections(Lists.newArrayList());
    }

    public void add(Section section) {
        if (!this.isEmpty() && !this.isEndOfStation(section)){
            adjustSections(section);
        }

        this.sections.add(section);
    }

    public List<Section> getSections() {
        return this.sections;
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }

        return getSortedStations();
    }

    public boolean containStationBySection(Section section) {
        return this.isExistStation(section.getUpStation())
            && this.isExistStation(section.getDownStation());
    }

    public boolean isEmpty() {
        return this.sections.isEmpty();
    }

    public boolean containUpDownStation(Section section) {
        return !this.isExistStation(section.getUpStation())
            && !this.isExistStation(section.getDownStation());
    }

    public void removeStation(Station station) {
        validateHasOnlyOneSectionWhenRemoveStation();
        validateNotIncludeStation(station);

        Optional<Section> upLineSection = findSectionByUpStation(station);
        Optional<Section> downLineSection = findSectionByDownStation(station);

        if (upLineSection.isPresent() && downLineSection.isPresent()) {
            addMiddleSection(upLineSection.get(), downLineSection.get());
        }

        upLineSection.ifPresent(this::remove);
        downLineSection.ifPresent(this::remove);
    }

    public Optional<Section> findSectionById(Long sectionId) {
        return StreamUtils.filterAndFindFirst(this.sections, section -> section.getId().equals(sectionId));
    }

    private void addMiddleSection(Section upLineSection, Section downLineSection) {
        Station newUpStation = downLineSection.getUpStation();
        Station newDownStation = upLineSection.getDownStation();
        Distance newDistance = Distance.merge(upLineSection.getDistance(), downLineSection.getDistance());

        sections.add(Section.of(downLineSection.getLine(), newUpStation, newDownStation, newDistance));
    }

    private void remove(Section section) {
        this.sections.remove(section);
    }

    private void validateHasOnlyOneSectionWhenRemoveStation() {
        if (this.getSections().size() <= 1) {
            throw new IllegalStateException(CANNOT_REMOVE_STATION_WHEN_ONLY_ONE_SECTIONS.getMessage());
        }
    }

    private void validateNotIncludeStation(Station station) {
        if (!isExistStation(station)) {
            throw new IllegalStateException(CANNOT_REMOVE_STATION_IS_NOT_EXIST.getMessage());
        }
    }

    private boolean isExistStation(Station station) {
        return this.getStations().contains(station);
    }

    private Optional<Section> findSectionByUpStation(Station station) {
        return StreamUtils.filterAndFindFirst(this.sections,
            section -> section.isEqualsUpStation(station));
    }

    private Optional<Section> findSectionByDownStation(Station station) {
        return StreamUtils.filterAndFindFirst(this.sections,
            section -> section.isEqualsDownStation(station));
    }

    private List<Station> getSortedStations() {
        List<Station> stations = Lists.newArrayList();
        stations.add(this.findFirstSection().getUpStation());
        for (int i = 0; i < sections.size(); i++) {
            Optional<Section> sectionByUpStation = findSectionByUpStation(stations.get(i));
            sectionByUpStation.map(Section::getDownStation)
                .ifPresent(stations::add);
        }
        return stations;
    }

    private Section findFirstSection() {
        List<Station> downStations = findDownStations();
        return StreamUtils.filterAndFindFirst(sections, section -> !downStations.contains(section.getUpStation()))
            .orElseThrow(() -> new IllegalStateException(NOT_EXIST_FIRST_SECTION.getMessage()));
    }

    private Section findLastSection() {
        List<Station> upStations = findUpStations();
        return StreamUtils.filterAndFindFirst(sections, section -> !upStations.contains(section.getDownStation()))
            .orElseThrow(() -> new IllegalStateException(NOT_EXIST_FIRST_SECTION.getMessage()));
    }

    private List<Station> findDownStations() {
        return StreamUtils.mapToList(this.sections, Section::getDownStation);
    }

    private List<Station> findUpStations() {
        return StreamUtils.mapToList(this.sections, Section::getUpStation);
    }

    private void adjustSections(Section section) {
        adjustUpStation(section);
        adjustDownStation(section);
    }

    private void adjustUpStation(Section section) {
        if (isExistStation(section.getUpStation())) {
            StreamUtils.filterAndFindFirst(this.sections, it -> it.getUpStation().equals(section.getUpStation()))
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
        }
    }

    private void adjustDownStation(Section section) {
        if (isExistStation(section.getDownStation())) {
            StreamUtils.filterAndFindFirst(this.sections, it -> it.getDownStation().equals(section.getDownStation()))
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
        }
    }

    private boolean isEndOfStation(Section section) {
        Section firstSection = this.findFirstSection();
        Section lastSection = this.findLastSection();

        return firstSection.isEqualsUpStation(section.getDownStation())
            || lastSection.isEqualsDownStation(section.getUpStation());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }
}
