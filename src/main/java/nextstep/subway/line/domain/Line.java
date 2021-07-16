package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

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
    private Fare fare;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color, Fare fare) {
        this.name = name;
        this.color = color;
        this.fare = fare;
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance, Fare fare) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
        this.fare = fare;
    }

    public Line(String name, String color, Section section, Fare fare) {
        this.name = name;
        this.color = color;
        this.sections.add(section);
        this.fare = fare;
    }

    public Line(long id, String name, String color, Station upStation, Station downStation, Distance distance, Fare fare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections.add(new Section(this, upStation, downStation, distance));
        this.fare = fare;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public void deleteStation(Station station) {
        sections.deleteStation(station);
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
        return sections.get();
    }

    public Stations getStations() {
        return sections.toStations();
    }

    public Fare getFare() {
        return fare;
    }

    public boolean containsStationsExactly(Stations stations) {
        boolean result = true;

        for (Station station : stations.get()) {
            result = (result && sections.toStations().contains(station));
        }

        return result;
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
