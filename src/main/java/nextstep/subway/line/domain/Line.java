package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        addSection(new Section(upStation, downStation, distance));
    }

    public void addSection(Section newSection) {
        this.sections.add(newSection);
        newSection.setLine(this);
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
        List<Section> sortedSections = new ArrayList<>();
        Section section = findFirstSection();
        sortedSections.add(section);

        while (sortedSections.size() < this.sections.size()) {
            section = findNextStation(section.getDownStation());
            sortedSections.add(section);
        }

        return Collections.unmodifiableList(sortedSections);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        List<Section> sortedSections = getSections();

        List<Station> stations = new ArrayList<>();
        stations.add(sortedSections.get(0).getUpStation());
        stations.addAll(getDownStations(sortedSections));
        return stations;
    }

    private List<Station> getDownStations(List<Section> sortedSections) {
        return sortedSections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    public Section findFirstSection() {
        List<Station> downStations = getDownStations();

        return sections.stream()
                .filter(section -> !downStations.contains(section.getUpStation()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("상행 종점역을 찾을 수 없습니다."));
    }

    private Section findNextStation(Station downStation) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(downStation))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("이어지는 구간을 찾을 수 없습니다."));
    }

    public List<Station> getDownStations() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    public void removeSection(Section section) {
        sections.remove(section);
    }
}
