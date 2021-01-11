package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
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

    @Embedded
    private Sections sections = new Sections();

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.getSections().add(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void updateUpStation(Station upStation, Station downStation, int distance) {
        sections.getSections().stream()
            .filter(it -> it.getUpStation() == upStation)
            .findFirst()
            .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    public void updateDownStation(Station upStation, Station downStation, int distance) {
        sections.getSections().stream()
            .filter(it -> it.getDownStation() == downStation)
            .findFirst()
            .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        sections.getSections().add(new Section(line, upStation, downStation, distance));
    }

    public Optional<Section> getContainUpStation(Station station) {
        return sections.getSections().stream()
            .filter(it -> it.getUpStation() == station)
            .findFirst();
    }
    public Optional<Section> getContainDownStation(Station station) {
        return sections.getSections().stream()
            .filter(it -> it.getDownStation() == station)
            .findFirst();
    }

    public List<Station> getStations() {
        if (sections.getSections().isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getContainUpStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = sections.getSections().get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getContainDownStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void removeLineStation(Station station) {
        Optional<Section> upLineStation = getContainUpStation(station);
        Optional<Section> downLineStation = getContainDownStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            addMergedSection(upLineStation, downLineStation);
        }

        upLineStation.ifPresent(it -> sections.getSections().remove(it));
        downLineStation.ifPresent(it -> sections.getSections().remove(it));
    }

    private void addMergedSection(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        Station newUpStation = downLineStation.get().getUpStation();
        Station newDownStation = upLineStation.get().getDownStation();
        int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
        addSection(this, newUpStation, newDownStation, newDistance);
    }

    public void addSection(Line line, List<Station> stations, Station upStation, Station downStation, int distance) {
        validateSections(stations, upStation, downStation);
        if (stations.isEmpty()) {
            addSection(line, upStation, downStation, distance);
            return;
        }

        if (upStation.isExisted(stations)) {
            updateUpStation(upStation, downStation, distance);
        } else if (downStation.isExisted(stations)) {
            updateDownStation(upStation, downStation, distance);
        } else {
            throw new RuntimeException();
        }

        addSection(line, upStation, downStation, distance);
    }

    private void validateSections(List<Station> stations, Station upStation, Station downStation) {
        if (upStation.isExisted(stations) && downStation.isExisted(stations)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
            stations.stream().noneMatch(it -> it == downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public Line() {

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
}
