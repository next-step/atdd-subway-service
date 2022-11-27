package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST,
        CascadeType.MERGE}, orphanRemoval = true)
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

    public Line(String name, String color, List<Section> sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
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
        stations.add(findFirstStation());
        stations.addAll(
            sections.stream()
                .sorted()
                .map(Section::getDownStation)
                .collect(Collectors.toList())
        );
        return stations;
    }

    private Optional<Section> findFromUpStation(final Station station) {
        return sections.stream()
            .filter(it -> it.getUpStation() == station)
            .findFirst();
    }

    private Optional<Section> findFromDownStation(final Station station) {
        return sections.stream()
            .filter(it -> it.getDownStation() == station)
            .findFirst();
    }

    private Station findFirstStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findFromDownStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void removeStation(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = findFromUpStation(station);
        Optional<Section> downLineStation = findFromDownStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance =
                upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(this, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
            stations.stream().noneMatch(it -> it == downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            sections
                .add(new Section(this, upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted) {
            findFromUpStation(upStation)
                .ifPresent(it -> it.updateUpStation(downStation, distance));

            sections
                .add(new Section(this, upStation, downStation, distance));
        } else if (isDownStationExisted) {
            findFromDownStation(downStation)
                .ifPresent(it -> it.updateDownStation(upStation, distance));

            sections
                .add(new Section(this, upStation, downStation, distance));
        } else {
            throw new RuntimeException();
        }
    }
}
