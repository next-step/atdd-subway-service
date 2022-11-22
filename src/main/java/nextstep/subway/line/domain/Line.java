package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

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

    public void addSection(Section section) {
        sections.add(section);
    }

    public void removeLineStation(Station station) {
        Optional<Section> upLineStation = getUpLineStation(station);
        Optional<Section> downLineStation = getDownLineStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            addNewLineStation(upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
    }

    private Optional<Section> getUpLineStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private Optional<Section> getDownLineStation(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private void addNewLineStation(Section upLineStation, Section downLineStation) {
        addSection(new Section(this, downLineStation.getUpStation(), upLineStation.getDownStation(),
                upLineStation.getDistance() + downLineStation.getDistance()));
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
}
