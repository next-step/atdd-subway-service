package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

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
    private Surcharge surcharge = Surcharge.nonSurcharge();

    @Embedded
    private final Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, int surcharge) {
        this.name = name;
        this.color = color;
        this.surcharge.changeSurcharge(surcharge);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        addSection(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
        this.surcharge.changeSurcharge(line.getSurcharge());
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

    public int getSurcharge() {
        return surcharge.getSurcharge();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public Stations getStationsBySections() {
        return sections.getStations();
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void remove(Station station) {
        sections.removeByStation(station);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id)
                && Objects.equals(name, line.name)
                && Objects.equals(color, line.color)
                && Objects.equals(surcharge, line.surcharge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, surcharge);
    }
}
