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

    @Column(unique = true)
    private String name;

    private String color;

    @Embedded
    private Sections sections = new Sections();

    @Embedded
    private AdditionalFare additionalFare = AdditionalFare.init();

    public Line() {
    }

    public Line(String name, String color) {
        this(name, color, null, null, 0, 0);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color, upStation, downStation, distance, 0);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int additionalFare) {
        this.name = name;
        this.color = color;
        sections.addLineStation(new Section(this, upStation, downStation, distance));
        this.additionalFare = AdditionalFare.from(additionalFare);
    }

    public void addLineStation(Station upStation, Station downStation, int distance) {
        sections.addLineStation(new Section(this, upStation, downStation, distance));
    }

    public void removeLineStation(Station station) {
        sections.removeLineStation(this, station);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
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

    public List<Section> getSections() {
        return sections.getSections();
    }

    public AdditionalFare getAdditionalFare() {
        return additionalFare;
    }
}
