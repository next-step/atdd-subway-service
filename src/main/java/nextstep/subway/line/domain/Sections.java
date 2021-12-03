package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import nextstep.subway.common.domain.Fare;
import nextstep.subway.common.exception.DuplicateDataException;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.springframework.util.Assert;

@Embeddable
public class Sections {

    private static final Sections EMPTY = new Sections(Collections.emptyList());

    private static final int MINIMUM_SECTION_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    @Transient
    private Map<Station, Section> upStationToSectionCache;

    @Transient
    private Map<Station, Section> downStationToSectionCache;

    protected Sections() {
    }

    private Sections(List<Section> sections) {
        Assert.notNull(sections, "지하철 구간 목록은 반드시 존재해야 합니다.");
        this.sections.addAll(sections);
    }

    public static Sections from(Section section) {
        Assert.notNull(section, "초기 구간은 반드시 존재해야 합니다.");
        return new Sections(Collections.singletonList(section));
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    static Sections empty() {
        return EMPTY;
    }

    Stations sortedStations() {
        Section nextSection = firstSection();
        List<Station> stations = new ArrayList<>(nextSection.stations());

        while (isExistByUpStation(nextSection.downStation())) {
            nextSection = findByUpStation(nextSection.downStation());
            stations.add(nextSection.downStation());
        }
        return Stations.from(stations);
    }

    void add(Section section) {
        validateAddition(section);
        cutOverlappingSection(section);
        sections.add(section);
        deleteSectionCaches();
    }

    void removeStation(Station station) {
        if (isNotExist(station)) {
            return;
        }
        validateSize();
        remove(station);
        deleteSectionCaches();
    }

    void setLine(Line line) {
        for (Section section : sections) {
            section.changeLine(line);
        }
    }

    Sections merge(Sections sections) {
        List<Section> newList = new ArrayList<>(this.sections);
        newList.addAll(sections.sections);
        return new Sections(newList);
    }

    public List<Section> list() {
        return Collections.unmodifiableList(sections);
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public Fare maxExtraFare() {
        if (isEmpty()) {
            throw new InvalidDataException("비어있는 구간들에서 추가 요금을 계산할 수 없습니다.");
        }
        return Collections.max(
            sections.stream()
                .map(Section::extraFare)
                .collect(Collectors.toList())
        );
    }

    private boolean isEmpty() {
        return sections.isEmpty();
    }

    private void remove(Station station) {
        removeWhenExistByUpStation(station);
        removeWhenExistByDownStation(station);
        addMergedSectionWhenLocatedBetween(station);
    }

    private void addMergedSectionWhenLocatedBetween(Station station) {
        if (isLocatedBetween(station)) {
            sections.add(findByUpStation(station).merge(findByDownStation(station)));
        }
    }

    private void removeWhenExistByUpStation(Station station) {
        if (isExistByUpStation(station)) {
            sections.remove(findByUpStation(station));
        }
    }

    private void removeWhenExistByDownStation(Station station) {
        if (isExistByDownStation(station)) {
            sections.remove(findByDownStation(station));
        }
    }

    private boolean isLocatedBetween(Station station) {
        return isExistByUpStation(station) && isExistByDownStation(station);
    }

    private void validateSize() {
        if (hasMinimumSize()) {
            throw new InvalidDataException("구간은 반드시 한 개 이상 존재해야 합니다.");
        }
    }

    private boolean hasMinimumSize() {
        return sections.size() == MINIMUM_SECTION_SIZE;
    }

    private Section firstSection() {
        return sections.stream()
            .filter(this::doesNotHavePreviousSection)
            .findFirst()
            .orElseThrow(() -> new InvalidDataException("첫 구간이 존재하지 않습니다."));
    }

    private boolean doesNotHavePreviousSection(Section section) {
        return !isExistByDownStation(section.upStation());
    }

    private void cutOverlappingSection(Section section) {
        if (isExist(section.upStation())) {
            cutSectionWhenExistByUpStation(section);
            return;
        }
        cutSectionWhenExistByDownStation(section);
    }

    private void cutSectionWhenExistByUpStation(Section section) {
        if (isExistByUpStation(section.upStation())) {
            findByUpStation(section.upStation()).cut(section);
        }
    }

    private void cutSectionWhenExistByDownStation(Section section) {
        if (isExistByDownStation(section.downStation())) {
            findByDownStation(section.downStation()).cut(section);
        }
    }

    private void validateAddition(Section section) {
        boolean isExistUpStation = isExist(section.upStation());
        boolean isExistDownStation = isExist(section.downStation());
        if (isExistUpStation && isExistDownStation) {
            throw new DuplicateDataException(String.format("%s은 이미 등록된 구간 입니다.", section));
        }
        if (!isExistUpStation && !isExistDownStation) {
            throw new InvalidDataException(String.format("%s은 등록할 수 없는 구간 입니다.", section));
        }
    }

    private boolean isExistByUpStation(Station station) {
        return upStationToSection().containsKey(station);
    }

    private boolean isExistByDownStation(Station station) {
        return downStationToSection().containsKey(station);
    }

    private Section findByUpStation(Station station) {
        return upStationToSection().get(station);
    }

    private Section findByDownStation(Station station) {
        return downStationToSection().get(station);
    }

    private boolean isExist(Station station) {
        return isExistByUpStation(station) || isExistByDownStation(station);
    }

    private boolean isNotExist(Station station) {
        return !isExist(station);
    }

    private Map<Station, Section> upStationToSection() {
        if (upStationToSectionCache == null) {
            upStationToSectionCache = sections.stream()
                .collect(Collectors.toMap(Section::upStation, section -> section));
        }
        return upStationToSectionCache;
    }

    private Map<Station, Section> downStationToSection() {
        if (downStationToSectionCache == null) {
            downStationToSectionCache = sections.stream()
                .collect(Collectors.toMap(Section::downStation, section -> section));
        }
        return downStationToSectionCache;
    }

    private void deleteSectionCaches() {
        downStationToSectionCache = null;
        upStationToSectionCache = null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sections sections = (Sections) o;
        return Objects.equals(this.sections, sections.sections);
    }

    @Override
    public String toString() {
        return "Sections{" +
            "sections=" + sections +
            '}';
    }
}
