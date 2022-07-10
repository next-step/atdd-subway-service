package nextstep.subway.line.domain;

import java.util.ArrayList;
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
    private Integer extraCharge;

    @Embedded
    private Sections sections = new Sections(new ArrayList<>());

    public Line() {
    }

    public Line(String name, String color, Integer extraCharge) {
        this.name = name;
        this.color = color;
        this.extraCharge = extraCharge;
    }

    public Line(String name, String color, int extraCharge, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.extraCharge = extraCharge;
        sections.addSection(new Section(this, upStation, downStation, distance));
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

    public Integer getExtraCharge() {
        return extraCharge;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void addSection(Section section) {
        section.updateLine(this);
        sections.addSection(section);
    }

    public void removeSection(Station station) {
        sections.removeSection(station);
    }
}
