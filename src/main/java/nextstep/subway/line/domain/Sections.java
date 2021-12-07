package nextstep.subway.line.domain;

import static nextstep.subway.utils.Utils.distinctByKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.exception.CannotAddException;
import nextstep.subway.exception.CannotDeleteException;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    private static final int MIN_SIZE = 1;
    private static final int START_SECTION_INDEX = 0;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections () {
    }

    public void add(Section section) {
        if (!contains(section)) {
            sections.add(section);
        }
    }

    public List<Station> getSortedStations() {
        return getSortedSections().stream()
            .flatMap(it -> it.getUpDownStations())
            .filter(distinctByKey(Station::getName))
            .collect(Collectors.toList());
    }

    public void remove(Section section) {
        section.removeLine();
        sections.remove(section);
    }

    public boolean contains(Section section) {
        return sections.stream()
            .anyMatch(it -> it.equalsStations(section));
    }

    public boolean isEmptyStation() {
        return getStationStream().collect(Collectors.toList()).isEmpty();
    }

    public boolean isMinSize() {
        return sections.size() <= MIN_SIZE;
    }

    public void updateOriginSectionByAdded(Section section) {
        updateByUpStation(section);
        updateByDownStation(section);
    }

    public void removeLineStation(Station station) {
        validateMinSize();
        Section removeSection = sections.stream()
            .filter(it -> it.isIncludeStation(station))
            .findAny()
            .orElseThrow(() -> new NotFoundException("역이 포함된 구간이 없습니다."));

        remove(removeSection);

        for (Section section: sections) {
            section.updateConnect(removeSection);
        }
    }

    boolean isAlreadySection(Section section) {
        return sections.stream()
            .anyMatch(it -> it.equalsStations(section));
    }

    boolean isIncludeStationOfSection(Section section) {
        return getStationStream()
            .anyMatch(section::isIncludeStation);
    }

    public void validateForAdded(Section section) {
        if (isAlreadySection(section)) {
            throw new CannotAddException("이미 등록된 구간 입니다.");
        }

        if (!isIncludeStationOfSection(section)) {
            throw new CannotAddException("등록할 수 없는 구간 입니다.");
        }
    }

    private void validateMinSize() {
        if (isMinSize()) {
            throw new CannotDeleteException("구간이 하나는 존재해야 합니다.");
        }
    }

    private void updateByUpStation(Section section) {
        sections.stream()
            .filter(it -> it.isUpStationOfSection(section))
            .findFirst()
            .ifPresent(it -> it.updateUpStationBySection(section));
    }

    private void updateByDownStation(Section section) {
        sections.stream()
            .filter(it -> it.isDownStationOfSection(section))
            .findFirst()
            .ifPresent(it -> it.updateDownStationBySection(section));
    }

    private List<Section> getSortedSections() {
        List<Section> list = new ArrayList<>();
        Section section = findFirstSection();
        list.add(section);

        Optional<Section>  optional = Optional.of(section);
        while (optional.isPresent()) {
            Section finalSection = section;
            optional = sections.stream()
                .filter(finalSection::isTheUpLine)
                .findFirst();
            section = optional.orElse(finalSection);
            optional.ifPresent(list::add);
        }

        return list;
    }

    private Section findFirstSection() {
        Section section = sections.get(START_SECTION_INDEX);
        Optional<Section>  optional = Optional.of(section);
        while (optional.isPresent()) {
            Section finalSection = section;
            optional = sections.stream()
                .filter(finalSection::isTheDownLine)
                .findFirst();
            section = optional.orElse(finalSection);
        }
        return section;
    }

    private Stream<Station> getStationStream() {
        return sections.stream()
            .flatMap(Section::getUpDownStations);
    }

    public List<SectionEdge> getSectionEdges() {
        return sections.stream()
            .map(it -> SectionEdge.from(it))
            .collect(Collectors.toList());
    }

}
