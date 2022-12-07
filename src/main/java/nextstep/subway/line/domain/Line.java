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
    private Sections sections = new Sections();
    private int additionalFare;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color, upStation, downStation, distance, 0);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int additionalFare) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
        sections.addLineStation(this, upStation, downStation, distance);
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


    public int getAdditionalFare() {
        return additionalFare;
    }

    public List<Station> getStations() {
        return sections.extractStations();
    }

    public void addLineStation(Station upStation, Station downStation, int distance) {
        sections.addLineStation(this, upStation, downStation, distance);

    }

    public void removeLineStation(Station station) {
        sections.removeLineStation(this, station);

    }
}
