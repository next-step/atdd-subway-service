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
        if (this.getSections().isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation(this);
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.getSections().stream()
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

    private Station findUpStation(Line line) {
        Station downStation = line.getSections().get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = line.getSections().stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void addSection(List<Station> stations, Station upStation, Station downStation, int distance) {
        if (stations.isEmpty()) {
            sections.add(new Section(this, upStation, downStation, distance));
            return;
        }

        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        validate(stations, isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            upSection(upStation)
            .ifPresent(it -> it.updateUpStation(downStation, distance));

            sections.add(new Section(this, upStation, downStation, distance));
        } else if (isDownStationExisted) {
            downSection(downStation)
            .ifPresent(it -> it.updateDownStation(upStation, distance));

            sections.add(new Section(this, upStation, downStation, distance));
        }
    }

    private void validate(List<Station> stations, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public void removeSection(Station station) {
        if (getSections().size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = upSection(station);
        Optional<Section> downLineStation = downSection(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(this, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(section -> getSections().remove(section));
        downLineStation.ifPresent(section -> getSections().remove(section));
    }

    private Optional<Section> upSection(Station station) {
        return getSections().stream()
                .filter(section -> section.getUpStation() == station)
                .findFirst();
    }

    private Optional<Section> downSection(Station station) {
        return getSections().stream()
                .filter(section -> section.getDownStation() == station)
                .findFirst();
    }
}
