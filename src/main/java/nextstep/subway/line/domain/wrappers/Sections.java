package nextstep.subway.line.domain.wrappers;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {
    private static final String DUPLICATE_SECTION_ERROR_MESSAGE = "%s, %s 구간은 이미 등록된 구간 입니다.";
    private static final String NOT_CONTAIN_STATION_ERROR_MESSAGE = "등록할 수 없는 구간 입니다.";
    private static final int MINIMUM_SECTIONS_SIZE = 1;
    private static final String SINGLE_SECTION_NOT_REMOVE_ERROR_MESSAGE = "구간이 하나만 존재하는 경우 삭제할 수 없습니다.";

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public void addSection(Section section) {
        checkValidDuplicateSection(section);
        sections.add(section);
    }

    public void updateSections(Section section) {
        validSection(section);
        Optional<Section> updateTargetSection = findSectionByUpStation(section);
        if (updateTargetSection.isPresent()) {
            Section targetSection = updateTargetSection.get();
            updateSection(section, targetSection, targetSection.getDownStation());
            sections.add(section);
            return;
        }
        updateTargetSection = findSectionByDownStation(section);
        if (updateTargetSection.isPresent()) {
            Section targetSection = updateTargetSection.get();
            updateSection(section, targetSection, targetSection.getUpStation());
        }
        sections.add(section);
    }

    public void removeSection(Station station, Line line) {
        checkValidSingleSection();

        Optional<Section> upLineStation = findSectionByUpStation(station);
        Optional<Section> downLineStation = findSectionByDownStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            Distance newDistance = upLineStation.get().createNewDistanceBySum(downLineStation.get());
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
    }

    private void updateSection(Section section, Section updateTargetSection, Station updateStation) {
        Distance newDistance = updateTargetSection.createNewDistanceBySubtract(section);
        updateTargetSection.updateUpStation(updateStation, newDistance);
    }

    private void validSection(Section section) {
        checkValidDuplicateSection(section);
        checkValidContainStations(section);
    }

    private Optional<Section> findSectionByDownStation(Station station) {
        return sections.stream()
                    .filter(it -> it.isSameDownStation(station))
                    .findFirst();
    }

    private Optional<Section> findSectionByUpStation(Station station) {
        return sections.stream()
                    .filter(it -> it.isSameUpStation(station))
                    .findFirst();
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

    private void checkValidSingleSection() {
        if (sections.size() <= MINIMUM_SECTIONS_SIZE) {
            throw new IllegalArgumentException(SINGLE_SECTION_NOT_REMOVE_ERROR_MESSAGE);
        }
    }

    private Optional<Section> findSectionByUpStation(Section section) {
        return sections.stream().filter(st -> st.isSameUpStation(section)).findFirst();
    }

    private Optional<Section> findSectionByDownStation(Section section) {
        return sections.stream().filter(st -> st.isSameDownStation(section)).findFirst();
    }

    private void checkValidContainStations(Section section) {
        boolean isContainStation = sections.stream().noneMatch(st -> st.isContainStation(section));
        if (sections.size() > 0 && isContainStation) {
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
