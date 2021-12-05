package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

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
        this.sections.add(new Section(this, upStation, downStation, distance));
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
        return sections.list();
    }

    private Station findFirstUpStation() {
        return this.sections.findFirstUpStation();
    }

    public Station findUpStation() {
        Station downStation = this.findFirstUpStation();

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findLineStation(it -> it.getDownStation() == finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    private boolean matchAnyStation(List<Station> stations, Station station) {
        return stations.stream().anyMatch(it -> it == station);
    }

    private boolean matchNoneStation(List<Station> stations, Station station) {
        return !matchAnyStation(stations, station);
    }

    private Optional<Section> findLineStation(Predicate<Section> express) {
        return this.getSections().stream()
                .filter(express)
                .findFirst();
    }

    public void addLineStation(Station upStation, Station downStation, Integer distance) {
        List<Station> stations = this.getStations();
        boolean isUpStationExisted = matchAnyStation(stations, upStation);
        boolean isDownStationExisted = matchAnyStation(stations, downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && matchNoneStation(stations, upStation) &&
                matchNoneStation(stations, downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            this.sections.add(new Section(this, upStation, downStation, distance));
            return;
        }

        if(isUpStationExisted) {
            findLineStation(it -> it.getUpStation() == upStation)
                    .ifPresent(it -> it.updateUpStation(downStation, distance));
            this.sections.add(new Section(this, upStation, downStation, distance));
            return;
        }

        findLineStation(it -> it.getDownStation() == downStation)
            .ifPresent(it -> it.updateDownStation(upStation, distance));
        this.sections.add(new Section(this, upStation, downStation, distance));
    }

    public List<Station> getStations() {
        if (this.getSections().isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = this.findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findLineStation(it -> it.getUpStation() == finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public void removeStation(Station station) {

        if (this.getSections().size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = findLineStation(it -> it.getUpStation() == station);
        Optional<Section> downLineStation = findLineStation(it -> it.getDownStation() == station);

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
