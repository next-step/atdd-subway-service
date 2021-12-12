package nextstep.subway.line.domain;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.*;
import static javax.persistence.CascadeType.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    private static final int ORDER_START_SECTION_IDX = 0;
    private static final int MIN_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {}

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    static Sections of(Section... sections) {
        return new Sections(new ArrayList<>(asList(sections)));
    }

    void add(Section otherSection) {
        validateNonDuplication(otherSection);
        validateContainStation(otherSection);
        for (Section section : sections) {
            section.connectIfHasEqualStation(otherSection);
        }
        sections.add(otherSection);
    }

    void removeSectionByStation(Station station) {
        validateMinSize();
        Section sectionToRemove = findSectionToRemoveBy(station);
        for (Section section : sections) {
            section.connectIfAdjacentByStation(sectionToRemove, station);
        }
        sections.remove(sectionToRemove);
    }

    List<Station> getStationsInOrder() {
        return getSectionsInOrder()
                .flatMap(Section::getStations)
                .distinct()
                .collect(toList());
    }

    int sumDistance() {
        return sections.stream()
                .mapToInt(Section::getDistance)
                .sum();
    }

    List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    private void validateNonDuplication(Section other) {
        boolean isUpStationExisted = sections.stream()
                .anyMatch(section -> section.hasEqualUpStation(other));

        boolean isDownStationExisted = sections.stream()
                .anyMatch(section -> isUpStationExisted && section.hasEqualDownStation(other));

        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
    }

    private void validateContainStation(Section otherSection) {
        if (sections.isEmpty()) {
            return;
        }
        boolean notContainStation = sections.stream()
                .flatMap(Section::getStations)
                .noneMatch(otherSection::contain);
        if (notContainStation) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    private Stream<Section> getSectionsInOrder() {
        Section section = sections.get(ORDER_START_SECTION_IDX);
        Stream<Section> upwardSections = getUpwardSectionsClosed(section);
        Stream<Section> downwardSections = getDownwardSectionsClosed(section);
        return concat(upwardSections, downwardSections).distinct();
    }

    private Stream<Section> getUpwardSectionsClosed(Section currentSection) {
        Optional<Section> upwardSection = sections.stream()
                .filter(section -> section.isUpwardOf(currentSection))
                .findAny();

        Stream<Section> current = Stream.of(currentSection);

        return upwardSection
                .map(upward -> concat(getUpwardSectionsClosed(upward), current))
                .orElse(current);
    }

    private Stream<Section> getDownwardSectionsClosed(Section currentSection) {
        Optional<Section> downwardSection = sections.stream()
                .filter(section -> section.isDownwardOf(currentSection))
                .findAny();

        Stream<Section> current = Stream.of(currentSection);

        return downwardSection
                .map(downward -> concat(current, getDownwardSectionsClosed(downward)))
                .orElse(current);
    }

    private void validateMinSize() {
        if (sections.size() == MIN_SIZE) {
            throw new IllegalArgumentException("노선의 남은 구간이 하나 밖에 없어 제거할 수 없습니다.");
        }
    }

    private Section findSectionToRemoveBy(Station station) {
        return sections.stream()
                .filter(section -> section.contain(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("노선에 없는 역의 구간은 제거할 수 없습니다."));
    }
}

