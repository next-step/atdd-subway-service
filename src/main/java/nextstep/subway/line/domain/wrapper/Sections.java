package nextstep.subway.line.domain.wrapper;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EntityExistsException;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Embeddable
public class Sections {
    private static final String EMPTY_SECTIONS = "등록된 구간이 없습니다.";
    private static final String ALREADY_EXISTS_SECTION = "이미 존재하는 구간입니다.";
    private static final String DUPLICATED_STATIONS = "이미 등록된 역 구간을 다시 등록 할 수 없습니다.";
    private static final String NOT_CONTAINS_NEITHER_STATIONS = "상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없습니다.";

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public static Sections newInstance() {
        return new Sections();
    }

    public void add(final Section section) {
        if (this.sections.contains(section)) {
            throw new EntityExistsException(ALREADY_EXISTS_SECTION);
        }
        this.sections.add(section);
    }

    public void registerNewSection(final Section newSection) {
        if (isFirstAdd()) {
            add(newSection);
            return;
        }
        addAfterFirstAddition(newSection);
    }

    private boolean isFirstAdd() {
        return this.sections.isEmpty();
    }

    private void addAfterFirstAddition(final Section newSection) {
        validateNewSection(newSection);
        for (Section section : this.sections) {
            section.updateSectionStationByAddNewSection(newSection);
        }
        add(newSection);
    }

    private void validateNewSection(final Section newSection) {
        Set<Station> stations = getStations();
        if (hasBothStation(stations, newSection)) {
            throw new IllegalArgumentException(DUPLICATED_STATIONS);
        }
        if (hasNotBothStation(stations, newSection)) {
            throw new IllegalArgumentException(NOT_CONTAINS_NEITHER_STATIONS);
        }
    }

    private boolean hasBothStation(final Set<Station> stations, final Section section) {
        return stations.contains(section.getUpStation()) && stations.contains(section.getDownStation());
    }

    private boolean hasNotBothStation(final Set<Station> stations, final Section section) {
        return !stations.contains(section.getUpStation()) && !stations.contains(section.getDownStation());
    }

    private Set<Station> getStations() {
        return this.sections.stream()
                .flatMap(section -> section.getUpAndDownStation().stream())
                .collect(toSet());
    }

    public Set<Station> getSortedStations() {
        List<Section> sortedSections = new ArrayList<>();
        Section firstSection = findFirstSection();
        sortedSections.add(firstSection);
        addNextSectionIfExist(findNextSection(firstSection), sortedSections);
        return sortedSections.stream()
                .flatMap(section -> section.getUpAndDownStation().stream())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private void addNextSectionIfExist(Optional<Section> maybeNextSection, List<Section> sortedSections) {
        if (!maybeNextSection.isPresent()) {
            return;
        }
        Section section = maybeNextSection.get();
        sortedSections.add(section);
        addNextSectionIfExist(findNextSection(section), sortedSections);
    }

    private Optional<Section> findNextSection(Section compare) {
        return sections.stream()
                .filter(origin -> !compare.equals(origin))
                .filter(origin -> origin.isAfter(compare))
                .findFirst();
    }

    private Section findFirstSection() {
        return sections.stream()
                .filter(this::isHead)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(EMPTY_SECTIONS));
    }

    private boolean isHead(Section compare) {
        return sections.stream()
                .filter(origin -> !compare.equals(origin))
                .noneMatch(compare::isAfter);
    }

    public List<Section> toCollection() {
        return sections;
    }
}
