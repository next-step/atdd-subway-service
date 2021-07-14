package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.common.domain.SubwayFare;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.math.BigDecimal;
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
    @Embedded
    private SubwayFare surcharge;

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

    public Line(String name, String color, Station upStation, Station downStation, int distance, BigDecimal surcharge) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
        this.surcharge = new SubwayFare(surcharge);
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

    public Sections getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public BigDecimal getSurcharge() {
        return surcharge.charged();
    }

    public void addStation(Station upStation, Station downStation, int distance) {
        sections.addStation(this, upStation, downStation, distance);
    }

    public void removeStation(Station station) {
        sections.removeStation(this, station);
    }

}
