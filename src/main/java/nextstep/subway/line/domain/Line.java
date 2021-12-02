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

    public Station findUpTerminalStation() {
        Station upStation = sections.get(0).getUpStation();
        while (hasSectionHavingDownStation(upStation)) {
            final Section preSection = findSectionHavingDownStation(upStation);
            upStation = preSection.getUpStation();
        }
        return upStation;
    }

    private boolean hasSectionHavingDownStation(Station station) {
        return sections.stream()
            .filter(Section::hasDownStation)
            .anyMatch(it -> it.equalsDownStation(station));
    }

    private Section findSectionHavingDownStation(Station station) {
        return sections.stream()
            .filter(it -> it.equalsDownStation(station))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("%s역을 하행역으로 갖는 구간이 없습니다.", station.getName())
            ));
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
