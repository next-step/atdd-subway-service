package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
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

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.size() == 0) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station station = findDepartureStation();
        stations.add(station);

        while (station != null) {
            Station finalStation = station;
            Optional<Section> optional = sections.stream()
                                                 .filter(sec -> sec.getUpStation().equals(finalStation))
                                                 .findFirst();
            if (!optional.isPresent()) {
                break;
            }
            station = optional.get().getDownStation();
            stations.add(station);
        }

        return stations;
    }

    public Station findDepartureStation() {
        Station station = sections.get(0).getUpStation();

        while (station != null) {
            Station finalStation = station;
            Optional<Section> departure = sections.stream()
                                                  .filter(sec -> sec.getDownStation().equals(finalStation))
                                                  .findFirst();
            if (!departure.isPresent()) {
                break;
            }
            station = departure.get().getUpStation();
        }

        return station;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color) && Objects.equals(sections, line.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, sections);
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", sections=" + sections +
                '}';
    }
}
