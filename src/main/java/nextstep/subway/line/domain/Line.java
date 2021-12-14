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
    private Fare extraFare;

    public Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name,color);
        sections.addSection(new Section(this, upStation, downStation, new Distance(distance)));
        this.extraFare = Fare.of(0);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int extraFare) {
        this(name, color, upStation, downStation, distance);
        this.extraFare = Fare.of(extraFare);
        sections.addSection(new Section(this, upStation, downStation, new Distance(distance)));
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
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

    public List<Station> getStations() {
        return this.sections.getStations();
    }

    public List<Section> getSections(){
        return this.sections.sections();
    }

    public void addSection(Section section) {
        this.sections.addSection(section);
    }

    public void removeStation(Station station) {
        this.sections.removeStation(this, station);
    }

    public Fare getExtraFare() {
        return extraFare;
    }
}
