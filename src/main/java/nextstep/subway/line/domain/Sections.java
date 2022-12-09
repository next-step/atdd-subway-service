package nextstep.subway.line.domain;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.station.domain.Station;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    @ReadOnlyProperty
    private final List<Section> sections;

    public Sections() {
        sections = new ArrayList<>();
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void add(Section section) {
        checkValidation(section);
        sections.add(section);
    }

    private void checkValidation(Section section) {
        checkDuplicatedSection(section);
    }

    private void checkDuplicatedSection(Section section) {
        if (sections.stream().anyMatch(eachSection -> eachSection.equals(section))) {
            throw new IllegalArgumentException(ErrorCode.NO_SAME_SECTION_EXCEPTION.getErrorMessage());
        }
    }


    public List<Station> getSortedStations() {
        List<Station> sortedStations = new ArrayList<>();
        Section firstSection = findFirstSection();
        firstSection.addStations(sortedStations);
        addNextStation(sortedStations, firstSection);
        return sortedStations;
    }

    private void addNextStation(List<Station> stations, Section previousSection) {
        Optional<Section> nextSection = findNextSection(previousSection);
        while (nextSection.isPresent()) {
            Section currentSection = nextSection.get();
            currentSection.addNextStation(stations);
            nextSection = findNextSection(currentSection);
        }
    }

    private Section findFirstSection() {
        Section firstSection = sections.get(0);
        Optional<Section> previousSection = findPreviousSection(firstSection);
        while (previousSection.isPresent()) {
            firstSection = previousSection.get();
            previousSection = findPreviousSection(firstSection);
        }
        return firstSection;
    }

    private Optional<Section> findNextSection(Section previousSection) {
        return sections.stream()
                .filter(section -> section.isEqualUpStationNewSectionDownStation(previousSection))
                .findFirst();
    }

    private Optional<Section> findPreviousSection(Section currentSection) {
        return sections.stream()
                .filter(section -> section.isEqualDownStationNewSectionUpStation(currentSection))
                .findFirst();
    }

    public List<Section> asList() {
        return this.sections;
    }
}
