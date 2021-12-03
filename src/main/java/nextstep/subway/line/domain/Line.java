package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private final SectionGroup sectionGroup = new SectionGroup();

    protected Line() {

    }

    protected Line(String name, String color, List<Section> sections) {
        this.name = name;
        this.color = color;
        sectionGroup.addAll(sections);
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color, new Section(upStation, downStation, distance));
    }

    public Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        sectionGroup.add(section);
    }

    public static Line of(String name, String color, List<Section> sections) {
        return new Line(name, color, sections);
    }

    public static Line of(String name, String color, Section section) {
        return new Line(name, color, section);
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
        return this.sectionGroup.getSections();
    }

    public List<Station> getStations() {
        return this.sectionGroup.getStations();
    }

    public void addSection(Section section) {
        sectionGroup.addSection(section);
    }
}
