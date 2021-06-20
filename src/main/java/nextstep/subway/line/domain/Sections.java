package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {

    public static final String SECTIONS_CANNOT_BE_NULL = "구간목록은 NULL이 될수 없습니다.";
    public static final String SECTION_ALREADY_EXISTS = "이미 상행역과 하행역으로 연결되는 구간이 등록되어 있습니다.";
    public static final String THERE_IS_NO_STATION_INCLUDED_BETWEEN_UP_AND_DOWN_STATIONS = "상행역과 하행역 둘중 포함되는 역이 없습니다.";

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public boolean add(Section newSection) {
        if (!sections.isEmpty()) {
            validationForAdd(newSection);
        }
        changeSectionWhenUpStationMatch(newSection);
        changeSectionWhenDownStationMatch(newSection);
        return sections.add(newSection);
    }

    private void changeSectionWhenUpStationMatch(Section newSection) {
        sections.stream()
            .filter(section -> section.isEqualUpStation(newSection))
            .findFirst()
            .ifPresent(section -> section.updateUpStation(newSection.getDownStation(), newSection.getDistance()));
    }

    private void changeSectionWhenDownStationMatch(Section newSection) {
        sections.stream()
            .filter(section -> section.isEqualDownStation(newSection))
            .findFirst()
            .ifPresent(section -> section.updateDownStation(newSection.getUpStation(), newSection.getDistance()));
    }

    private void validationForAdd(Section section) {
        if (isPresent(section)) {
            throw new IllegalArgumentException(SECTION_ALREADY_EXISTS);
        }
        if (!isPresentAnyStation(section)) {
            throw new IllegalArgumentException(THERE_IS_NO_STATION_INCLUDED_BETWEEN_UP_AND_DOWN_STATIONS);
        }
    }

    private boolean isPresent(Section section) {
        return sections.stream()
            .anyMatch(s -> s.isEqualAllStation(section));
    }

    private boolean isPresentAnyStation(Section section) {
        return sections.stream()
            .anyMatch(s -> s.isPresentAnyStation(section));
    }

    public List<Section> getSections() {
        return sections;
    }
}
