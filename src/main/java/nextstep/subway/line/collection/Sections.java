package nextstep.subway.line.collection;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Sections {
    public static final String NOT_REGISTERED_EXCEPTION = "등록할 수 없는 구간 입니다.";
    public static final String LINE_MINIMUM_SECTION_EXCEPTION = "노선의 구간의 최소 구간은 1구간입니다.";
    public static final int LINE_MINIMUM_SECTION = 1;
    public static final String NOT_REGIST_STATION_TO_LINE_EXCEPTION = "이 노선에 등록되지 않은 역입니다.";

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
        Section upSection = getUpSection(stationId);
        Section downSection = getDownSection(stationId);
        if (downSection == null) {
            sections.remove(upSection);
            return true;
        }
        if (upSection == null) {
            sections.remove(downSection);
            return true;
        }
        upSection.connectNewSection(downSection);
        sections.remove(downSection);
        return true;
    }

    private Section getUpSection(Long stationId) {
        return sections.stream()
                .filter(it -> it.getSectionSameDownStation(stationId))
                .findFirst()
                .orElse(null);
    }

    private Section getDownSection(Long stationId) {
        return sections.stream()
                .filter(it -> it.getSectionSameUpStation(stationId))
                .findFirst()
                .orElse(null);
    }

    private void checkRemoveStationValidate(Long stationId) {
        if (sections.size() <= LINE_MINIMUM_SECTION) {
            throw new IllegalArgumentException(LINE_MINIMUM_SECTION_EXCEPTION);
        }
        if (sections.stream().noneMatch(it -> it.getSectionSameDownStation(stationId) || it.getSectionSameUpStation(stationId))) {
            throw new IllegalArgumentException(NOT_REGIST_STATION_TO_LINE_EXCEPTION);
        }
    }

    private void checkSectionValidate(Section newSection) {
        int sectionIndex = 0;
        boolean isSameUpStation = false;
        boolean isSameDownStation = false;
        Section oldSection;
        while (!isSameUpStation && !isSameDownStation && sectionIndex < sections.size()) {
            oldSection = sections.get(sectionIndex);
            isSameUpStation = isSameUpStationOfSection(oldSection, newSection);
            isSameDownStation = isSameDownStationOfSection(oldSection, newSection);
            sectionIndex++;
        }
        if (isSameUpStation || isSameDownStation) return;
        if (isUpFinalSection(newSection)) return;
        if (isDownFinalSection(newSection)) return;
        throw new IllegalArgumentException(NOT_REGISTERED_EXCEPTION);
    }

    private boolean isUpFinalSection(Section newSection) {
        return sections.stream()
                .anyMatch(it -> it.isUpFinalSection(newSection));
    }

    private boolean isDownFinalSection(Section newSection) {
        return sections.stream()
                .anyMatch(it -> it.isDownFinalSection(newSection));
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
        sortedStations.add(upFinalSection.getUpStation());
        Station nextStation;
        Section nextSection = upFinalSection;
        while (nextSection != null) {
            nextStation = nextSection.getDownStation();
            sortedStations.add(nextStation);
            nextSection = getNextStation(nextStation);
        }
        return sortedStations;
    }

    private Section getUpFinalStation() {
        return sections.stream()
                .filter(it -> getDownStationOfUpStation(it.getUpStation()) == null)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private Section getDownStationOfUpStation(Station upStation) {
        return sections.stream()
                .filter(it -> it.isSameDownStation(upStation))
                .findFirst()
                .orElse(null);
    }

    private Section getNextStation(Station downStation) {
        return sections.stream()
                .filter(it -> it.isSameUpStation(downStation))
                .findFirst()
                .orElse(null);
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
