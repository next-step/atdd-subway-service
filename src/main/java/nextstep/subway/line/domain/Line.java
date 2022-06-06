package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.Optional;
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

    protected Line() {
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

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }
        List<Station> stations = new ArrayList<>();
        Section firstSection = this.firstSection();
        stations.add(firstSection.getUpStation());
        stations.addAll(restStations(firstSection));
        return stations;
    }

    private List<Station> restStations(Section firstSection) {
        List<Station> stations = new ArrayList<>();
        Section targetSection = firstSection;
        Optional<Section> nextSection = findNextSection(targetSection);
        while (nextSection.isPresent()) {
            stations.add(nextSection.get().getUpStation());
            targetSection = nextSection.get();
            nextSection = findNextSection(targetSection);
        }
        stations.add(targetSection.getDownStation());
        return stations;
    }

    private Section firstSection(){
        return findFirstSection(sections.get(0));
    }

    private Section findFirstSection(Section initSection) {
        Section targetSection = initSection;
        Optional<Section> prevSection = findPrevSection(targetSection);
        while (prevSection.isPresent()) {
            targetSection = prevSection.get();
            prevSection = findPrevSection(targetSection);
        }
        return targetSection;
    }

    private Optional<Section> findPrevSection(Section currentSection){
        return sections.stream()
                .filter(section -> section.getDownStation() == currentSection.getUpStation())
                .findFirst();
    }

    private Optional<Section> findNextSection(Section currentSection){
        return sections.stream()
                .filter(section -> section.getUpStation() == currentSection.getDownStation())
                .findFirst();
    }
}
