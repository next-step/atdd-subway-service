package nextstep.subway.line.domain;

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

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance) {
        this.name = name;
        this.color = color;
        sections.registerNewSection(new Section(this, upStation, downStation, distance));
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

    public List<Station> getEndToEndStations() {
        return new ArrayList<>(sections.getDistinctStations());

    }

    public void addSection(Section newSection) {
        newSection.addLine(this);
        sections.registerNewSection(newSection);
    }

    public List<Section> getSections() {
        return sections.getLineSections();
    }

    public void removeStation(Long stationId) {
        sections.removeStation(stationId);
    }
}
