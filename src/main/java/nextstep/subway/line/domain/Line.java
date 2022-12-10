package nextstep.subway.line.domain;

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
    private AdditionalFee additionalFee;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, AdditionalFee additionalFee) {
        this(name, color);
        this.additionalFee = additionalFee;
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance) {
        this(name, color);
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance,
        AdditionalFee additionalFee) {
        this(name, color, upStation, downStation, distance);
        this.additionalFee = additionalFee;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
        this.additionalFee = line.getAdditionalFee();
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
        return this.sections.getStations();
    }

    public Sections getSections() {
        return sections;
    }

    public AdditionalFee getAdditionalFee() {
        return additionalFee;
    }

    public void addSection(Station upStation, Station downStation, Distance distance) {
        this.sections.add(new Section(this, upStation, downStation, distance));
    }

    public void removeStation(Station station) {
        this.sections.remove(station);
    }
}
