package nextstep.subway.line.domain;

import nextstep.subway.line.application.exception.InvalidSectionException;
import nextstep.subway.line.application.exception.SectionNotFoundException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    private static final int MIN_SECTION_SIZE = 1;
    private static final String NOT_CONNECTABLE = "구간을 연결할 상행역 또는 하행역이 존재해야 합니다.";
    private static final String NOT_FOUND_TERMINUS = "상행 종점역을 찾을 수 없습니다.";
    private static final String BREAK_SECTION = "이어지는 구간을 찾을 수 없습니다.";
    private static final String NOT_DELETE_MIN_SECTION_SIZE = "노선의 구간이" + MIN_SECTION_SIZE + "개 이하인 경우 구간을 삭제할 수 없습니다.";

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
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
                .orElseThrow(() -> new InvalidSectionException(NOT_CONNECTABLE));

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
                .orElseThrow(() -> new SectionNotFoundException(NOT_FOUND_TERMINUS));
    }

    private Section findNextStation(Station downStation) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(downStation))
                .findFirst()
                .orElseThrow(() -> new SectionNotFoundException(BREAK_SECTION));
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
            throw new InvalidSectionException(NOT_DELETE_MIN_SECTION_SIZE);
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
