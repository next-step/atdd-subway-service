package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private static final String CAN_NOT_FIND_FIRST_SECTION = "can not find first section.";
    private static final String BOTH_ALREADY_ADDED = "both stations is already added.";
    private static final String BOTH_NOT_CONTAINS = "both stations is not contains.";
    private static final String SECTIONS_HAS_ONLY_ONE = "sections has only one.";
    private static final String NOT_CONTAINS_STATION = "not contains station.";
    private static final int REMOVABLE_SIZE = 2;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section section) {
        if (isFirstNewSection()) {
            sections.add(section);
            return;
        }
        addNotFirstNewSection(section);
    }

    private boolean isFirstNewSection() {
        return sections.isEmpty();
    }

    private void addNotFirstNewSection(Section newSection) {
        checkAddable(newSection);
        sections.forEach(section -> section.update(newSection));
        sections.add(newSection);
    }

    private void checkAddable(Section section) {
        List<Station> stations = getSortedStation();
        if (isBothAdded(section, stations)) {
            throw new IllegalArgumentException(BOTH_ALREADY_ADDED);
        }
        if (isBothNotContains(section, stations)) {
            throw new IllegalArgumentException(BOTH_NOT_CONTAINS);
        }
    }

    private boolean isBothAdded(Section section, List<Station> stations) {
        return stations.containsAll(section.getUpDownStation());
    }

    private boolean isBothNotContains(Section section, List<Station> stations) {
        return !stations.contains(section.getUpStation()) && !stations.contains(section.getDownStation());
    }

    public List<Station> getSortedStation() {
        List<Section> sortedSection = getSortedSection();
        LinkedHashSet<Station> sortedSet = sortedSection.stream()
                .flatMap(section -> section.getUpDownStation().stream())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return new ArrayList<>(sortedSet);
    }

    private List<Section> getSortedSection() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Section> sortedSections = new ArrayList<>();
        Section firstSection = getFirstSection();
        sortedSections.add(firstSection);
        addNotFirstSections(sortedSections, firstSection);
        return sortedSections;
    }

    private Section getFirstSection() {
        return sections.stream()
                .filter(this::isFirst)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(CAN_NOT_FIND_FIRST_SECTION));
    }

    private boolean isFirst(Section section) {
        return sections.stream()
                .filter(it -> !section.equals(it))
                .noneMatch(section::isNext);
    }

    private void addNotFirstSections(List<Section> sortedSections, Section before) {
        Optional<Section> optionalNext = getNextSection(before);
        if (!optionalNext.isPresent()) {
            return;
        }
        Section next = optionalNext.get();
        sortedSections.add(next);
        addNotFirstSections(sortedSections, next);
    }

    private Optional<Section> getNextSection(Section section) {
        return sections.stream()
                .filter(it -> it.isNext(section))
                .findFirst();
    }

    public void remove(Station station) {
        checkRemovable(station);
        Section firstSection = getFirstSection();
        Section lastSection = getLastSection();
        if (firstSection.isUpStation(station)) {
            this.sections.remove(firstSection);
            return;
        }
        if (lastSection.isDownStation(station)) {
            this.sections.remove(lastSection);
            return;
        }
        removeMiddleSection(station);
    }

    private void checkRemovable(Station station) {
        if (isInvalidSectionsSize()) {
            throw new IllegalArgumentException(SECTIONS_HAS_ONLY_ONE);
        }
        if (!getSortedStation().contains(station)) {
            throw new IllegalArgumentException(NOT_CONTAINS_STATION);
        }
    }

    private Section getLastSection() {
        return sections.stream()
                .filter(this::isDown)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("can not find section."));
    }

    private boolean isDown(Section section) {
        return sections.stream()
                .filter(it -> !section.equals(it))
                .noneMatch(section::isBefore);
    }

    private boolean isInvalidSectionsSize() {
        return sections.size() < REMOVABLE_SIZE;
    }

    private void removeMiddleSection(Station station) {
        Section beforeSection = getBeforeSection(station);
        Section afterSection = getAfterSection(station);
        addCombinationSection(beforeSection, afterSection);
        removeBeforeAfterSection(beforeSection, afterSection);
    }

    private Section getBeforeSection(Station station) {
        return sections.stream()
                .filter(section -> section.isDownStation(station))
                .findFirst().orElseThrow(EntityNotFoundException::new);
    }

    private Section getAfterSection(Station station) {
        return sections.stream()
                .filter(section -> section.isUpStation(station))
                .findFirst().orElseThrow(EntityNotFoundException::new);
    }

    private void addCombinationSection(final Section beforeSection, final Section afterSection) {
        Section combinedSection = beforeSection.combine(afterSection);
        this.sections.add(combinedSection);
    }

    private void removeBeforeAfterSection(Section beforeSection, Section afterSection) {
        this.sections.remove(beforeSection);
        this.sections.remove(afterSection);
    }
}
