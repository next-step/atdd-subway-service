package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.exeption.CanNotDeleteStateException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.exeption.CanNotAddStationException;
import nextstep.subway.station.exeption.RegisteredStationException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Entity
public class Line extends BaseEntity {

    public static final int MIN_SIZE = 1;

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
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getSections().stream()
                .filter(it -> it.getUpStation() == finalDownStation)
                .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = getSections().get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getSections().stream()
                .filter(it -> it.getDownStation() == finalDownStation)
                .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }
        return downStation;
    }
    public void addStation(Station upStation, Station downStation, int distance) {
        validateStation(upStation, downStation);

        sections.stream()
                .filter(s -> s.getUpStation().equals(upStation) || s.getDownStation().equals(downStation))
                .findFirst()
                .ifPresent(s -> {
                    s.updateStation(upStation, downStation, distance);
                });
        sections.add(new Section(this, upStation, downStation, distance));
    }

    private void validateStation(Station upStation, Station downStation) {
        if (!sections.isEmpty() && containsStation(upStation) && containsStation(downStation)) {
            throw new RegisteredStationException();
        }
        if (!sections.isEmpty() &&!containsStation(upStation) && !containsStation(downStation)) {
            throw new CanNotAddStationException();
        }
    }
    public boolean containsStation(Station station) {
        return sections.stream().anyMatch(s -> s.containStation(station));
    }

    public void removeStation(Station station) {
        if (canNotDelete()) {
            throw new CanNotDeleteStateException();
        }

        Optional<Section> upLineStation = getSections().stream()
            .filter(it -> it.getUpStation() == station)
            .findFirst();
        Optional<Section> downLineStation = getSections().stream()
            .filter(it -> it.getDownStation() == station)
            .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            getSections().add(new Section(this, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> getSections().remove(it));
        downLineStation.ifPresent(it -> getSections().remove(it));
    }

    private boolean canNotDelete() {
        return sections.size() == MIN_SIZE;
    }
}
