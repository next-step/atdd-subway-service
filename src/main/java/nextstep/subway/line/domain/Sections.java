package nextstep.subway.line.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    private static final int ONE_SECTION_SIZE = 1;
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections;

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = new LinkedList(sections);
    }

    public List<Section> value() {
        return this.sections;
    }

    public Set<Station> orderedStations() {
        Set<Station> sortedStations = new LinkedHashSet<>();
        Optional<Section> section = findFirstSection();
        while (section.isPresent()) {
            section.ifPresent(findSection -> sortedStations.addAll(findSection.stations()));
            section = findNextSection(section.get());
        }
        return sortedStations;
    }

    private List<Station> stations() {
        return this.sections.stream()
                .map(Section::stations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    private Optional<Section> findFirstSection() {
        List<Station> downStations = this.sections.stream().map(Section::getDownStation).collect(Collectors.toList());
        return this.sections.stream()
                .filter(section -> !downStations.contains(section.getUpStation()))
                .findFirst();
    }
    private Optional<Section> findNextSection(Section currentSection) {
        return this.sections.stream()
                .filter(section -> section.isNext(currentSection))
                .findFirst();
    }

    public void add(Section newSection) {
        validateHasStations(newSection);
        validateHasNotBothStations(newSection);
        sections.forEach(section -> section.updateStation(newSection));
        sections.add(newSection);
    }

    private void validateHasStations(Section newSection) {
        if (new HashSet<>(orderedStations()).containsAll(newSection.stations())) {
            throw new IllegalArgumentException("등록하려는 역이 모두 존재합니다.");
        }
    }

    private void validateHasNotBothStations(Section newSection) {
        if (notInclude(newSection)) {
            throw new IllegalArgumentException("상행역과 하행역 모두 존재하지 않습니다.");
        }
    }

    private boolean notInclude(Section newSection) {
        List<Station> assignedStations = this.stations();
        return newSection.stations().stream()
                .noneMatch(assignedStations::contains);
    }

    public void delete(Station station) {
        validateNotIncludeStation(station);
        validateLastSection();
        Optional<Section> prevSection = findPrevSection(station);
        Optional<Section> nextSection = findNextSection(station);
        if (isMiddleSection(prevSection, nextSection)) {
            deleteMiddleSection(prevSection.get(), nextSection.get());
            return;
        }
        deleteEndSection(prevSection, nextSection);
    }

    private void validateLastSection() {
        if (sections.size() == ONE_SECTION_SIZE) {
            throw new IllegalArgumentException("마지막 구간은 삭제할 수 없습니다.");
        }
    }

    private void validateNotIncludeStation(Station station) {
        boolean isNotInclude = this.stations().stream().noneMatch(station::equals);
        if (isNotInclude) {
            throw new IllegalArgumentException("노선에 포함되지 않은 지하철 역은 삭제할 수 없습니다.");
        }
    }

    private void deleteEndSection(Optional<Section> prevSection, Optional<Section> nextSection) {
        nextSection.ifPresent(sections::remove);
        prevSection.ifPresent(sections::remove);
    }

    private void deleteMiddleSection(Section prevSection, Section nextSection) {
        prevSection.merge(nextSection);
        this.sections.remove(nextSection);
    }

    private Optional<Section> findPrevSection(Station station) {
        return this.sections.stream()
                .filter(section -> section.isDownStation(station))
                .findFirst();
    }

    private Optional<Section> findNextSection(Station station) {
        return this.sections.stream()
                .filter(section -> section.isUpStation(station))
                .findFirst();
    }

    private boolean isMiddleSection(Optional<Section> prevSection, Optional<Section> nextSection) {
        return prevSection.isPresent() && nextSection.isPresent();
    }
}
