package nextstep.subway.line.domain;

import java.util.Objects;
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

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST,
        CascadeType.MERGE}, orphanRemoval = true)
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

    public void addSection(Section section) {
        this.sections.add(section);
    }

    @Deprecated
    public Long getId() {
        return id;
    }

    @Deprecated
    public String getName() {
        return name;
    }

    @Deprecated
    public String getColor() {
        return color;
    }

    @Deprecated
    public List<Section> getSections() {
        return sections;
    }

    public Integer sectionsCount() {
        return sections.size();
    }

    public boolean sameNameAndColor(Line line) {
        return Objects.equals(this.color, line.color)
            && Objects.equals(this.name, line.name);
    }
}
