package nextstep.subway.line.domain;

import java.util.Collections;
import java.util.Set;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private Sections sections;

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        Section section = new Section(upStation, downStation, distance);
        section.addLine(this);
        this.sections = new Sections(Collections.singletonList(section));
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
        return sections.value();
    }

    public Set<Station> findStations() {
        return sections.orderedStations();
    }

    public void addSection(Section newSection) {
        sections.add(newSection);
        newSection.addLine(this);
    }

}
