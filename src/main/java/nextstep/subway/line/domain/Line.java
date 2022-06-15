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

    @Embedded
    private final Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.addSection(new Section(this, upStation, downStation, distance));
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
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        return sections.getStations();
    }

    public void addLineStation(Section section) {
        sections.addSection(section);
    }

    public void removeLineStation(Station station) {
//        validateStationCount();
//        validateStationInclusion(station);
//
//        if (isUpStation(station)) {
//            Section section = sections.stream()
//                                      .filter(sec -> sec.getUpStation().equals(station))
//                                      .findFirst().orElseThrow(RuntimeException::new);
//            sections.stream()
//                    .filter(sec -> sec.getDownStation().equals(station))
//                    .findFirst()
//                    .ifPresent(sec -> sec.updateDownStation(section.getDownStation(), -section.getDistance()));
//            sections.remove(section);
//            return;
//        }
//
//        Section section = sections.stream()
//                                  .filter(sec -> sec.getDownStation().equals(station))
//                                  .findFirst().orElseThrow(RuntimeException::new);
//
//        sections.stream()
//                .filter(sec -> sec.getUpStation().equals(station))
//                .findFirst()
//                .ifPresent(sec -> sec.updateUpStation(section.getUpStation(), -section.getDistance()));
//        sections.remove(section);
    }

    public void validateStationCount() {
//        if (sections.size() <= 1) {
//            throw new RuntimeException("마지막 구간은 삭제할 수 없습니다.");
//        }
    }

    public void validateStationInclusion(Station station) {
//        if (getStations().stream().noneMatch(st -> st.equals(station))) {
//            throw new RuntimeException(String.format("%s 노선에 %s 역이 존재하지 않습니다.", name, station.getName()));
//        }
    }

//    public boolean isUpStation(Station station) {
//        return sections.stream().anyMatch(section -> section.getUpStation().equals(station));
//    }

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
