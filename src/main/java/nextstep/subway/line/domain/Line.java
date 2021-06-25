package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
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
        if(isSameUpStation || isSameDownStation) return;
        if(isUpFinalSection(newSection)) return;
        if(isDownFinalSection(newSection)) return;
        throw new RuntimeException("등록할 수 없는 구간 입니다.");
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
