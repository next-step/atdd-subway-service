package nextstep.subway.line.domain;

import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    private Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color, new Section( upStation, downStation, Distance.from(distance)));
    }

    public Line(String name, String color, Section section) {
        this(name, color);
        addSection(section);
    }

    public static Line from(String name, String color) {
        return new Line(name, color);
    }

    public void addSection(Section section) {
        sections.addSection(section);
        section.toLine(this);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void removeLineStation(Station station) {
        sections.removeLineStation(this, station);
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

    public List<Station> getStations() {
        return Collections.unmodifiableList(sections.getStations());
    }
}
