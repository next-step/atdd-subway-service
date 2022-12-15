package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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
    private Fare fare;

    protected Line() {

    }

    public Line(String name,
                String color,
                Station upStation,
                Station downStation,
                int distance) {
        this(name, color);
        this.addSection(upStation, downStation, distance);
    }

    public Line(String name,
                String color,
                Station upStation,
                Station downStation,
                int distance,
                int fare) {
        this(name, color, fare);
        this.addSection(upStation, downStation, distance);
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.fare = Fare.zero();
    }

    public Line(String name, String color, int fare) {
        this.name = name;
        this.color = color;
        this.fare = Fare.of(fare);
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

    public void addSection(Station upStation, Station downStation, int distance) {
        Section section = new Section(this, upStation, downStation, new Distance(distance));
        sections.add(section);
    }

    public void removeStation(Station station) {
        this.sections.removeStation(station);
    }


    public List<Section> getSections() {
        return this.sections.getAll();
    }

    public Fare getFare() {
        return this.fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        if (!Objects.equals(id, line.id)) return false;
        return Objects.equals(name, line.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", fare='" + fare + '\'' +
                '}';
    }
}
