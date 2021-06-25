package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.collection.Distance;
import nextstep.subway.line.collection.Sections;
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
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.sections.addSection(this, new Section(upStation, downStation, new Distance(distance)));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public boolean addSection(Station upStation, Station downStation, int distance) {
        this.sections.addSection(this, new Section(upStation, downStation, new Distance(distance)));
        return true;
    }

    public boolean removeStation(Long stationId) {
        return this.sections.removeStation(stationId);
    }

    public List<Station> getStations() {
        return sections.getStations();
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
}
