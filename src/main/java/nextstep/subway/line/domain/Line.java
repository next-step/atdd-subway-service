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
    private ExtraFare extraFare = new ExtraFare();

    @Embedded
    private final Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color, ExtraFare extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line(String name, String color, ExtraFare extraFare, Station upStation, Station downStation, Distance distance) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
        this.extraFare = line.getExtraFare();
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

    public ExtraFare getExtraFare() {
        return extraFare;
    }

    public Sections getSections() {
        return sections;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public List<Station> getOrderedStations() {
        return sections.getOrderedStations();
    }

    public void removeLineStation(Station station) {
        sections.remove(station);
    }
}
