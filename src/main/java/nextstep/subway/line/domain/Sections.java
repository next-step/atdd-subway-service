package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    static final String NOT_CONNECTED_ERR_MSG = "추가되는 구간은 기존의 구간과 연결 가능하여야 합니다.";
    static final String ALREADY_EXIST_ERR_MSG = "상행역과 하행역이 이미 노선에 모두 등록되어 있습니다.";
    static final String LAST_SECTION_CANNOT_BE_REMOVED_ERR_MSG = "노선의 마지막 구간은 삭제할 수 없습니다.";
    static final String NOT_FOUND_ERR_MSG = "노선에 등록되어 있지 않은 역은 삭제할 수 없습니다.";
    private static final String NOT_FOUND_UP_EDGE_SECTION_ERR_MSG = "지하철 노선에 상행 종점 구간이 존재하지 않습니다.";

    private static final int MIN_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    void addSection(final Line line, final Section section) {
        if (!sections.isEmpty()) {
            validateAddable(section);

            sections.stream()
                .filter(it -> it.isOverlapped(section))
                .findAny()
                .ifPresent(it -> it.divideBy(section));
        }

        section.setLine(line);
    }

    void removeStation(final Station station) {
        validateDeletable(station);

        final Iterator<Section> matchedIterator = sections.stream()
            .filter(it -> it.getUpStation().equals(station) || it.getDownStation().equals(station))
            .iterator();
        final Section toBeDeleted = matchedIterator.next();
        if (matchedIterator.hasNext()) {
            matchedIterator.next().connectWith(toBeDeleted);
        }
        toBeDeleted.removeLine();
    }

    private void validateAddable(final Section section) {
        final Set<Station> allStations = extractAllStations();
        final List<Station> sectionStations = section.getStations();

        final Set<Station> matchedStations = sectionStations.stream()
            .filter(allStations::contains)
            .collect(Collectors.toSet());

        if (matchedStations.isEmpty()) {
            throw new BadRequestException(NOT_CONNECTED_ERR_MSG);
        }
        if (matchedStations.containsAll(sectionStations)) {
            throw new BadRequestException(ALREADY_EXIST_ERR_MSG);
        }
    }

    private void validateDeletable(final Station station) {
        if (sections.size() == MIN_SIZE) {
            throw new BadRequestException(LAST_SECTION_CANNOT_BE_REMOVED_ERR_MSG);
        }

        final Set<Station> allStations = extractAllStations();
        if (!allStations.contains(station)) {
            throw new BadRequestException(NOT_FOUND_ERR_MSG);
        }
    }

    List<Station> computeSortedStations() {
        final List<Station> sortedStations = new ArrayList<>();
        final Section upEdgeSection = computeUpEdgeSection();
        sortedStations.add(upEdgeSection.getUpStation());
        sortedStations.add(upEdgeSection.getDownStation());

        final List<Station> nextStations = computeNextSections(upEdgeSection).stream()
            .map(Section::getDownStation)
            .collect(Collectors.toList());

        sortedStations.addAll(nextStations);

        return sortedStations;
    }

    private Section computeUpEdgeSection() {
        final Set<Station> downStations = extractDownStations();

        return sections.stream()
            .filter(section -> !downStations.contains(section.getUpStation()))
            .findAny()
            .orElseThrow(() -> new IllegalStateException(NOT_FOUND_UP_EDGE_SECTION_ERR_MSG));
    }

    private List<Section> computeNextSections(final Section previousSection) {
        final List<Section> nextSections = new ArrayList<>();

        final Optional<Section> nextSection = sections.stream()
            .filter(section -> section.isNextSection(previousSection))
            .findAny();

        if (nextSection.isPresent()) {
            nextSections.add(nextSection.get());
            nextSections.addAll(computeNextSections(nextSection.get()));
        }

        return nextSections;
    }

    Set<Station> extractAllStations() {
        return sections.stream()
            .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
            .collect(Collectors.toSet());
    }

    private Set<Station> extractDownStations() {
        return sections.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toSet());
    }

    List<Section> getSections() {
        return sections;
    }
}
