package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String color;
    @Embedded
    private final Sections sections = new Sections();
    @Column(nullable = false)
    private Integer surcharge;

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance, Integer surcharge) {
        this.name = name;
        this.color = color;
        addSection(upStation, downStation, distance);
        this.surcharge = surcharge;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void addSection(Station upStation, Station downStation, Distance distance) {
        sections.addSection(this, upStation, downStation, distance);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
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

    public List<Section> getSections() {
        return sections.getSections();
    }

    public Integer getSurcharge() {
        return surcharge;
    }
}
