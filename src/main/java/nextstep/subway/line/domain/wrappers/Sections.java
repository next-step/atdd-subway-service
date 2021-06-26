package nextstep.subway.line.domain.wrappers;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import javax.persistence.Embeddable;
import java.util.*;

@Embeddable
public class Sections {
    private static final String DUPLICATE_SECTION_ERROR_MESSAGE = "%s, %s 구간은 이미 등록된 구간 입니다.";
    public static final String NOT_CONTAIN_STATION_ERROR_MESSAGE = "등록할 수 없는 구간 입니다.";
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void addSection(Section section) {
        checkValidDuplicateSection(section);
        checkValidContainStations(section);
        Optional<Section> updateTargetSection = findSectionByUpStation(section);
        if (updateTargetSection.isPresent()) {
            Section targetSection = updateTargetSection.get();
            int newDistance = targetSection.getDistance() - section.getDistance();
            targetSection.updateUpStation(section.getDownStation(), newDistance);
            sections.add(section);
            return;
        }
        updateTargetSection = findSectionByDownStation(section);
        if (updateTargetSection.isPresent()) {
            Section targetSection = updateTargetSection.get();
            int newDistance = targetSection.getDistance() - section.getDistance();
            targetSection.updateDownStation(section.getUpStation(), newDistance);
        }
        sections.add(section);
    }

    public List<Station> stations() {
        List<Station> stations = new ArrayList<>();
        if (sections.isEmpty()) {
            return stations;
        }
        Section preSection = findFirstSection();
        stations.add(preSection.getUpStation());
        while (preSection != null) {
            Station downStation = preSection.getDownStation();
            stations.add(downStation);
            preSection = findNextSection(preSection);
        }
        return stations;
    }

    private Optional<Section> findSectionByUpStation(Section section) {
        return sections.stream().filter(st -> st.isSameUpStation(section)).findFirst();
    }

    private Optional<Section> findSectionByDownStation(Section section) {
        return sections.stream().filter(st -> st.isSameDownStation(section)).findFirst();
    }

    private void checkValidContainStations(Section section) {
        boolean isContainStation = sections.stream().noneMatch(st -> st.isContainStation(section));
        if (isContainStation) {
            throw new IllegalArgumentException(NOT_CONTAIN_STATION_ERROR_MESSAGE);
        }

    }

    private Section findFirstSection() {
        Section section = null;
        for (Section st : sections) {
            section = st.calcFirstSection(section);
        }
        return section;
    }

    private Section findNextSection(Section section) {
        return sections.stream().filter(st -> st.isNextSection(section)).findFirst().orElse(null);
    }

    private void checkValidDuplicateSection(Section section) {
        if (sections.stream().anyMatch(st -> st.isSameStations(section))) {
            throw new IllegalArgumentException(
                    String.format(DUPLICATE_SECTION_ERROR_MESSAGE,
                            section.getUpStation().getName(), section.getDownStation().getName()));
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Sections sections1 = (Sections) object;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }
}
