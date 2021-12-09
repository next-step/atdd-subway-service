package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.*;
import static nextstep.subway.line.application.exception.InvalidSectionException.CAN_NOT_DELETE;
import static nextstep.subway.line.application.exception.InvalidSectionException.NOT_CONNECTABLE;
import static nextstep.subway.line.application.exception.SectionNotFoundException.BREAK_SECTION;
import static nextstep.subway.line.application.exception.SectionNotFoundException.NOT_FOUND_TERMINUS;

@Embeddable
public class Sections {
    private static final int MIN_SECTION_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {PERSIST, MERGE, REMOVE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void add(Section newSection) {
        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }
        addSection(newSection);
    }

    private void addSection(Section newSection) {
        Section section = sections.stream()
                .filter(oldSection -> oldSection.isConnectable(newSection))
                .findFirst()
                .map(oldSection -> oldSection.merge(newSection))
                .orElseThrow(() -> NOT_CONNECTABLE);

        sections.add(section);
    }

    public List<Section> getSections() {
        List<Section> sortedSections = new ArrayList<>();
        Section section = findFirstSection();
        sortedSections.add(section);

        while (sortedSections.size() < this.sections.size()) {
            section = findNextStation(section.getDownStation());
            sortedSections.add(section);
        }

        return Collections.unmodifiableList(sortedSections);
    }

    private Section findFirstSection() {
        List<Station> downStations = getDownStations();

        return sections.stream()
                .filter(section -> !downStations.contains(section.getUpStation()))
                .findFirst()
                .orElseThrow(() -> NOT_FOUND_TERMINUS);
    }

    private Section findNextStation(Station downStation) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(downStation))
                .findFirst()
                .orElseThrow(() -> BREAK_SECTION);
    }

    private List<Station> getDownStations() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public void remove(Station station) {
        if (sections.size() <= MIN_SECTION_SIZE) {
            throw CAN_NOT_DELETE;
        }

        removeUpSection(station);
        removeDownSection(station);
    }

    private void removeUpSection(Station station) {
        sections.stream()
                .filter(section -> section.equalUpStation(station))
                .findFirst()
                .ifPresent(this::mergeRemove);
    }

    private void mergeRemove(Section deleteSection) {
        sections.stream()
                .filter(section -> section.equalDownStation(deleteSection.getUpStation()))
                .findFirst()
                .ifPresent(preSection -> {
                    preSection.mergeDistance(preSection.getDistance());
                    preSection.changeDownStationLink(deleteSection.getDownStation());
                });
        sections.remove(deleteSection);
    }

    private void removeDownSection(Station station) {
        sections.stream()
                .filter(section -> section.equalDownStation(station))
                .findFirst()
                .ifPresent(section -> sections.remove(section));
    }
}
