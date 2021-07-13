package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Stream;

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

    @Embedded
    private Fee fee;

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.sections = Sections.of();
    }
    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.sections = Sections.of(this, upStation, downStation, distance);
        this.fee = Fee.ofWithOverFare(0);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int overFare) {
        this.name = name;
        this.color = color;
        this.sections = Sections.of(this, upStation, downStation, distance);
        this.fee = Fee.ofWithOverFare(overFare);
    }

    public void addLineStation(Station upStation, Station downStation, int distance) {
        sections.addStation(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void removeStation(Station station) {
        sections.removeStation(this, station);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public Stream<Section> getSectionsStream() {
        return sections.getSections().stream();
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

    public Fee getFee() {
        return fee;
    }
}