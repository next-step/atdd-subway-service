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
            Optional<Section> departure = sections.stream()
                                                  .filter(sec -> sec.getUpStation().equals(finalStation))
                                                  .findFirst();
            if (!departure.isPresent()) {
                break;
            }
            station = departure.get().getDownStation();
            stations.add(station);
        }

        return stations;
    }

    public Station findDepartureStation() {
        Station station = sections.get(0).getUpStation();

        while (station != null) {
            Station finalStation = station;
            Optional<Section> departure = sections.stream()
                                                  .filter(section -> section.getDownStation().equals(finalStation))
                                                  .findFirst();
            if (!departure.isPresent()) {
                break;
            }
            station = departure.get().getUpStation();
        }

        return station;
    }

    public void addLineStation(Section section) {
        List<Station> stations = getStations();
        if (stations.isEmpty()) {
            sections.add(section);
            return;
        }

        boolean isUpStationExisted = isUpStationExisted(stations, section);
        boolean isDownStationExisted = isDownStationExisted(stations, section);
        validateRequestSection(isUpStationExisted, isDownStationExisted);
        validateRequestParameter(stations, section);

        if (isUpStationExisted) {
            sections.stream()
                    .filter(it -> it.getUpStation() == section.getUpStation())
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            sections.add(section);
        } else if (isDownStationExisted) {
            sections.stream()
                    .filter(it -> it.getDownStation() == section.getDownStation())
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

            sections.add(section);
        } else {
            throw new RuntimeException();
        }
    }

    public boolean isUpStationExisted(List<Station> stations, Section section) {
        return stations.stream().anyMatch(it -> it == section.getUpStation());
    }

    public boolean isDownStationExisted(List<Station> stations, Section section) {
        return stations.stream().anyMatch(it -> it == section.getDownStation());
    }

    public void validateRequestSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    public void validateRequestParameter(List<Station> stations, Section section) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == section.getUpStation()) &&
                stations.stream().noneMatch(it -> it == section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
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
