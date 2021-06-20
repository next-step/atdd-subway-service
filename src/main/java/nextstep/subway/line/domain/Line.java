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

    public void addSection(Section newSection) {
        Station upStation = newSection.getUpStation();
        Station downStation = newSection.getDownStation();
        int distance = newSection.getDistance();
        boolean isUpStationExisted =this.sections.stream().anyMatch(section -> section.getUpStation().equals(upStation));
        boolean isDownStationExisted =this.sections.stream().anyMatch(section -> section.getDownStation().equals(downStation));

        if (isDownStationExisted && isUpStationExisted){
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (isDownStationExisted == false && isUpStationExisted == false){
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

       if (isUpStationExisted){
           updateUpStationWhenEqualsUpStations(newSection, upStation, downStation, distance);
           return;
       }

       if (isDownStationExisted){
           updateDownStationWhenEqualsDownStations(newSection, upStation, downStation, distance);
           return;
       }
    }

    private void updateDownStationWhenEqualsDownStations(Section newSection, Station upStation, Station downStation, int distance) {
        this.sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(section -> {
                    int findIndex = sections.indexOf(section);
                    section.updateDownStation(upStation, distance);
                    this.sections.add(findIndex+1, newSection);
                });
    }

    private void updateUpStationWhenEqualsUpStations(Section newSection, Station upStation, Station downStation, int distance) {
        this.sections.stream()
                .filter(it -> it.getUpStation() == upStation)
//                    같은 상행선역이 있는 섹터를 찾아
                .findFirst()
//                    같은 상행선역이 있는 섹터를 찾아
                .ifPresent(section -> {
                    int findIndex = sections.indexOf(section);
                    section.updateUpStation(downStation, distance);
                    this.sections.add(findIndex, newSection);
                });
    }
}
