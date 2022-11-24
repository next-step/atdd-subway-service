package nextstep.subway.line.domain;

import java.util.Collection;
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
    private static final String DUPLICATE_UP_DOWN_STATIONS = "상행역과 하행역이 이미 모두 노선에 등록되어 있습니다.";
    private static final String NOT_INCLUDE_UP_DOWN_STATIONS = "상행역과 하행역 모두 노선에 포함되어 있지 않습니다.";
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

    public Set<Station> assignedOrderedStation() {
        Set<Station> sortedStations = new LinkedHashSet<>();
        Optional<Section> section = findFirstSection();
        while (section.isPresent()) {
            section.ifPresent(findSection -> sortedStations.addAll(findSection.stations()));
            section = findNextSection(section.get());
        }
        return sortedStations;
    }

    private List<Station> assignedStations() {
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
        validateDuplicateUpDownStation(newSection);
        validateNotIncludeUpDownStation(newSection);
        sections.forEach(section -> section.reorganize(newSection));
        sections.add(newSection);
    }

    private void validateDuplicateUpDownStation(Section newSection) {
        boolean isSame = this.sections.stream().anyMatch(section -> section.isSameUpDownStation(newSection));
        if (isSame) {
            throw new IllegalArgumentException(DUPLICATE_UP_DOWN_STATIONS);
        }
    }

    private void validateNotIncludeUpDownStation(Section newSection) {
        if (notInclude(newSection)) {
            throw new IllegalArgumentException(NOT_INCLUDE_UP_DOWN_STATIONS);
        }
    }

    private boolean notInclude(Section newSection) {
        List<Station> assignedStations = this.assignedStations();
        return newSection.stations().stream()
                .noneMatch(assignedStations::contains);
    }
}
