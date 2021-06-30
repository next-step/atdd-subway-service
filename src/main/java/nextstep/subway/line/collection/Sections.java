package nextstep.subway.line.collection;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {
    private static final int LINE_MINIMUM_SECTION = 1;

    @OneToMany(mappedBy = "line", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections;

    public Sections() {
        this.sections = new ArrayList<>();
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public boolean addSection(Line line, Section newSection) {
        if (!sections.isEmpty()) {
            checkSectionValidate(newSection);
        }
        sections.add(newSection);
        newSection.setLine(line);
        return true;
    }

    public boolean removeStation(Long stationId) {
        checkRemoveStationValidate(stationId);
        Optional<Section> upSection = getUpSection(stationId);
        Optional<Section> downSection = getDownSection(stationId);
        if (!downSection.isPresent()) {
            sections.remove(upSection);
            return true;
        }
        if (!upSection.isPresent()) {
            sections.remove(downSection);
            return true;
        }
        upSection.get().connectNewSection(downSection.get());
        sections.remove(downSection);
        return true;
    }

    private Optional<Section> getUpSection(Long stationId) {
        return sections.stream()
                .filter(section -> section.getSectionSameDownStation(stationId))
                .findFirst();
    }

    private Optional<Section> getDownSection(Long stationId) {
        return sections.stream()
                .filter(section -> section.getSectionSameUpStation(stationId))
                .findFirst();
    }

    private void checkRemoveStationValidate(Long stationId) {
        if (sections.size() <= LINE_MINIMUM_SECTION) {
            throw new IllegalArgumentException("노선의 구간의 최소 구간은 1구간입니다.");
        }
        if (sections.stream().noneMatch(section -> section.getSectionSameDownStation(stationId) || section.getSectionSameUpStation(stationId))) {
            throw new IllegalArgumentException("이 노선에 등록되지 않은 역입니다.");
        }
    }

    private void checkSectionValidate(Section newSection) {
        boolean isPossibleResister = sections.stream().anyMatch(section -> isPossibleResister(section, newSection));
        if (!isPossibleResister) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    public boolean isPossibleResister(Section oldSection, Section newSection) {
        boolean isSameUpStation = isSameUpStationOfSection(oldSection, newSection);
        boolean isSameDownStation = isSameDownStationOfSection(oldSection, newSection);
        if (isSameUpStation || isSameDownStation) return true;
        if (isUpFinalSection(newSection)) return true;
        if (isDownFinalSection(newSection)) return true;
        return false;
    }

    private boolean isUpFinalSection(Section newSection) {
        return sections.stream()
                .anyMatch(section -> section.isUpFinalSection(newSection));
    }

    private boolean isDownFinalSection(Section newSection) {
        return sections.stream()
                .anyMatch(section -> section.isDownFinalSection(newSection));
    }

    private boolean isSameUpStationOfSection(Section oldSection, Section newSection) {
        oldSection.isSameSection(newSection);
        return oldSection.isSameUpStationOfSection(newSection);
    }

    private boolean isSameDownStationOfSection(Section oldSection, Section newSection) {
        return oldSection.isSameDownStationOfSection(newSection);
    }

    public List<Station> getStations() {
        List<Station> sortedStations = new ArrayList<>();
        Section upFinalSection = getUpFinalStation();
        Station downStation = upFinalSection.getDownStation();
        sortedStations.addAll(Arrays.asList(upFinalSection.getUpStation(), downStation));
        Optional<Section> nextSection;
        while ((nextSection = getNextStation(downStation)).isPresent()) {
            downStation = nextSection.get().getDownStation();
            sortedStations.add(downStation);
        }
        return sortedStations;
    }

    private Section getUpFinalStation() {
        return sections.stream()
                .filter(section -> isExistPreStationOfUpStation(section.getUpStation()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private boolean isExistPreStationOfUpStation(Station upStation) {
        return sections.stream()
                .noneMatch(section -> section.isSameDownStation(upStation));
    }

    private Optional<Section> getNextStation(Station downStation) {
        return sections.stream()
                .filter(section -> section.isSameUpStation(downStation))
                .findFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }
}
