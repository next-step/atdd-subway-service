package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

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

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        Optional<Section> firstSection = getFirstSection();

        Set<Station> result = new LinkedHashSet<>();
        while (firstSection.isPresent()) {
            Section currentSection = firstSection.get();
            result.addAll(currentSection.getStations());
            firstSection = sections.stream()
                    .filter(section -> Objects.equals(section.getUpStation(), currentSection.getDownStation()))
                    .findFirst();
        }
        return new ArrayList<>(result);
    }

    private Optional<Section> getFirstSection() {
        Section currentSection = null;
        Optional<Section> anySection = sections.stream()
                .findFirst();

        while (anySection.isPresent()) {
            currentSection = anySection.get();
            anySection = getPreviousSection(currentSection);
        }
        return Optional.ofNullable(currentSection);
    }

    private Optional<Section> getPreviousSection(Section currentSection) {
        return sections.stream()
                .filter(section -> Objects.equals(section.getDownStation(), currentSection.getUpStation()))
                .findFirst();
    }
}
