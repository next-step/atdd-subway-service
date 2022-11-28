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

    private List<Station> stations() {
        return this.sections.stream()
                .map(Section::stations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
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
        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }
        validateHasStations(newSection);
        validateHasNotBothStations(newSection);
        sections.forEach(section -> section.updateStation(newSection));
        sections.add(newSection);
    }

    private void validateHasStations(Section newSection) {
        if (new HashSet<>(orderedStations()).containsAll(newSection.findStations())) {
            throw new IllegalArgumentException("등록하려는 역이 모두 존재합니다.");
        }
    }

    private void validateHasNotBothStations(Section newSection) {
        if (hasNotBothStations(newSection)) {
            throw new IllegalArgumentException("상행성과 하행선 모두 존재하지 않습니다.");
        }
    }

    private boolean hasNotBothStations(Section section) {
        Set<Station> stations = orderedStations();
        return section.stations()
                .stream()
                .noneMatch(stations::contains);
    }

}
