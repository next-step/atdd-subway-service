package nextstep.subway.line.domain;

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

    public Set<Station> assignedStations() {
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
}
