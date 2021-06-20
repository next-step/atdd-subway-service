package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

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

    public Line() {}

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

    public List<StationResponse> getStations() {
        List<StationResponse> stations = new ArrayList<>();
        Section nextSection = findStartSection();

        if (sections.isEmpty() || nextSection == null) {
            return stations;
        }

        stations.add(StationResponse.of(nextSection.getUpStation()));
        while (nextSection != null) {
            stations.add(StationResponse.of(nextSection.getDownStation()));
            nextSection = findNextSection(nextSection);
        }

        return stations;

    }

    private Section findNextSection(Section beforSection) {
        return sections.stream().filter(section -> section.isUpStationWithDown(beforSection))
            .findFirst()
            .orElse(null);
    }

    private Section findStartSection() {
        return sections.stream()
            .filter(section -> findSectionIsAnotherDownStation(section) == null)
            .findFirst()
            .orElse(null);
    }

    private Section findSectionIsAnotherDownStation(Section beforeSection) {
        return sections.stream()
            .filter(section -> section.isDownStationWithUp(beforeSection))
            .findFirst()
            .orElse(null);
    }

}
