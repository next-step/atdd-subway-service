package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Collections;
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
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, long distance) {
        this.name = name;
        this.color = color;
        sections = new Sections(createSection(upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(Station upStation, Station downStation, long distance) {
        sections.add(createSection(upStation, downStation, distance));
    }

    public void deleteStation(Station station) {
        sections.deleteStation(station);
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
        if(sections.isEmpty()) {
            return Collections.emptyList();
        }
        return sections.getStations();
    }

    private Section createSection(Station upStation, Station downStation, long distance) {
        return new Section(this, upStation, downStation, distance);
    }
}
