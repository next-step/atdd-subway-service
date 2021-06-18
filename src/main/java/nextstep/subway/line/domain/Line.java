package nextstep.subway.line.domain;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.*;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    // TODO : 리팩터링 후 삭제 하기
    public void setSections(Section... sections) {
        this.sections = asList(sections);
    }

    public List<Station> getStationsInOrder() {
        return getSectionsInOrder()
            .flatMap(Section::getStations)
            .distinct()
            .collect(toList());
    }

    private Stream<Section> getSectionsInOrder() {
        Section section = sections.get(0);
        Stream<Section> upwardSections = getUpwardSectionsClosed(section);
        Stream<Section> downwardSections = getDownwardSectionsClosed(section);
        return concat(upwardSections, downwardSections).distinct();
    }

    private Stream<Section> getUpwardSectionsClosed(Section currentSection) {
        Optional<Section> upwardSection = sections.stream()
            .filter(section -> section.isUpwardOf(currentSection))
            .findAny();

        Stream<Section> current = of(currentSection);

        return upwardSection
            .map(upward -> concat(getUpwardSectionsClosed(upward), current))
            .orElse(current);
    }

    private Stream<Section> getDownwardSectionsClosed(Section currentSection) {
        Optional<Section> downwardSection = sections.stream()
            .filter(section -> section.isDownwardOf(currentSection))
            .findAny();

        Stream<Section> current = of(currentSection);

        return downwardSection
            .map(downward -> concat(current, getDownwardSectionsClosed(downward)))
            .orElse(current);
    }
}

