package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.consts.ErrorMessage;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
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
        if (getSections().isEmpty()) {
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

    public void addSection(Section newSection) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == newSection.getUpStation());
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == newSection.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException(ErrorMessage.ERROR_SECTION_ALREADY_EXIST);
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == newSection.getUpStation()) &&
                stations.stream().noneMatch(it -> it == newSection.getDownStation())) {
            throw new RuntimeException(ErrorMessage.ERROR_SECTION_NOT_REGISTER);
        }

        if (stations.isEmpty()) {
            getSections().add(newSection);
            return;
        }

        if (isUpStationExisted) {
            getSections().stream()
                    .filter(it -> it.getUpStation() == newSection.getUpStation())
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(newSection.getDownStation(), newSection.getDistance()));

            getSections().add(newSection);
        } else if (isDownStationExisted) {
            getSections().stream()
                    .filter(it -> it.getDownStation() == newSection.getDownStation())
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(newSection.getUpStation(), newSection.getDistance()));

            getSections().add(newSection);
        } else {
            throw new RuntimeException();
        }
    }

    public void removeStation(Station station) {
        if (getSections().size() <= 1) {
            throw new RuntimeException();
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
}
