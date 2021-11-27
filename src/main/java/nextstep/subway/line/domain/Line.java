package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
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

    @Embedded
    private Sections sections = new Sections();

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
        return sections.getSections();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void addLineStation(Station upStation, Station downStation, int distance) {
        sections.checkUpdatable(upStation, downStation);
        sections.updateStation(upStation, downStation, distance);
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void removeLineStation(Station station) {
        if (this.getSections().size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = sections.findUpSection(station);
        Optional<Section> downLineStation = this.getSections().stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            this.getSections().add(new Section(this, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> this.getSections().remove(it));
        downLineStation.ifPresent(it -> this.getSections().remove(it));
    }
}
