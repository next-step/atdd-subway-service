package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(final Section section) {
        this.sections.add(section);
    }

    public Set<Station> getStations() {
        Set<Station> sortedStations = new LinkedHashSet<>();
        Optional<Section> optionalSection = findFirstSection();
        while (optionalSection.isPresent()) {
            Section section = optionalSection.get();
            sortedStations.addAll(section.stations());
            optionalSection = findNextSection(section);
        }
        return sortedStations;
    }

    private Optional<Section> findFirstSection() {
        List<Station> downStations = this.sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        return this.sections.stream()
                .filter(section -> !downStations.contains(section.getUpStation()))
                .findFirst();
    }

    private Optional<Section> findNextSection(final Section currentSection) {
        return this.sections.stream()
                .filter(section -> section.isNextOf(currentSection))
                .findFirst();
    }

    public List<Section> value() {
        return sections;
    }

    public void add(final Section section) {
        sections.add(section);
    }

    public Optional<Section> findSameUpStation(final Section section) {
        return sections.stream()
                .filter(section::isSameUpStation)
                .findFirst();
    }

    public Optional<Section> findSameDownStation(final Section section) {
        return sections.stream()
                .filter(section::isSameDownStation)
                .findFirst();
    }

    public int count() {
        return sections.size();
    }
}
