package nextstep.subway.line.domain;

import java.util.Collections;
import java.util.List;
import java.util.Set;
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

    public String getColor() {
        return color;
    }

    public Set<Station> findStations() {
        return sections.orderedStations();
    }

    public void addSection(Section newSection) {
        newSection.addLine(this);
        this.sections.add(newSection);
    }

    public List<Section> getSections() {
        return this.sections.sections();
    }

    public List<Station> getStations() {
        return this.sections.stations();
    }

    public void deleteStation(Station station) {
        this.sections.delete(station);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
