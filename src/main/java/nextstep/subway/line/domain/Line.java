package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    public static final String NOT_REGISTERED_EXCEPTION = "등록할 수 없는 구간 입니다.";
    public static final String LINE_MINIMUM_SECTION_EXCEPTION = "노선의 구간의 최소 구간은 1구간입니다.";
    public static final int LINE_MINIMUM_SECTION = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public boolean addSection(Station upStation, Station downStation, int distance) {
        Section newSection = new Section(upStation, downStation, distance);
        if (!sections.isEmpty()) {
            checkSectionValidate(newSection);
        }
        sections.add(newSection);
        newSection.setLine(this);
        return true;
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

    public boolean removeStation(Long stationId) {
        checkRemoveStationValidate(stationId);
        Section upSection = getUpSection(stationId);
        Section downSection = getDownSection(stationId);
        if(downSection == null) {
            sections.remove(upSection);
            return true;
        }
        if(upSection == null) {
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
            throw new IllegalArgumentException("이 노선에 등록되지 않은 역입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections;
    }
}
