package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.domain.exceptions.CannotFindLineEndUpStationException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Station findUpStation() {
        validateNotEmptySections();

        Section currentSection = this.sections.get(0);
        Section theFirstSection = currentSection;

        if (currentSection == null) {
            throw new CannotFindLineEndUpStationException("해당 노선의 상행종점역을 찾을 수 없습니다.");
        }

        while(currentSection != null) {
            theFirstSection = currentSection;
            currentSection = findPreviousSection(currentSection);
        }

        return theFirstSection.getUpStation();
    }

    private Section findPreviousSection(Section thatSection) {
        return this.sections.stream()
                .filter(it -> thatSection.getUpStation() == it.getDownStation())
                .findFirst()
                .orElse(null);
    }

    private boolean isEmptySections() {
        return this.sections.size() == 0;
    }

    private void validateNotEmptySections() {
        if (isEmptySections()) {
            throw new CannotFindLineEndUpStationException("해당 노선의 상행종점역을 찾을 수 없습니다.");
        }
    }
}
