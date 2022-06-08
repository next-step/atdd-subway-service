package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

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

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }

        return getFromToLastStations(findUpStation());
    }

    public void addStation(Station upStation, Station downStation, int distance) {
        List<Station> stations = this.getStations();
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
            this.sections.add(new Section(this, upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted) {
            this.sections.stream()
                    .filter(it -> it.getUpStation() == upStation)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            this.sections.add(new Section(this, upStation, downStation, distance));
        } else if (isDownStationExisted) {
            this.sections.stream()
                    .filter(it -> it.getDownStation() == downStation)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, distance));

            this.sections.add(new Section(this, upStation, downStation, distance));
        } else {
            throw new RuntimeException();
        }
    }

    public void removeStation(Station station) {
        if (this.sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = this.sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = this.sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            this.sections.add(new Section(this, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> this.sections.remove(it));
        downLineStation.ifPresent(it -> this.sections.remove(it));
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

    private Station findUpStation() {
        Station downStation = this.getSections().get(0).getUpStation();

        return findStationByPredicateWithExecuteAction(
                downStation,
                null,
                (section, station) -> section.getDownStation() == station,
                Section::getUpStation
        );
    }

    private List<Station> getFromToLastStations(Station startStation) {
        List<Station> stations = new ArrayList<>();
        stations.add(startStation);

        findStationByPredicateWithExecuteAction(
                startStation,
                stations::add,
                (section, station) -> section.getUpStation() == station,
                Section::getDownStation
        );

        return stations;
    }

    private Station findStationByPredicateWithExecuteAction(
            Station firstStation, Consumer<Station> action,
            BiPredicate<Section, Station> findNextFilter,
            Function<Section, Station> findNextTarget
    ) {
        boolean isEnd = false;
        Station downStation = firstStation;

        while (!isEnd) {
            Station finalDownStation = downStation;
            Section nextSection = this.getSections().stream()
                    .filter(it -> findNextFilter.test(it, finalDownStation))
                    .findFirst()
                    .orElse(null);

            isEnd = (nextSection == null);
            downStation = findNextStationAndAfterProcessing(downStation, nextSection, action, findNextTarget);
        }

        return downStation;
    }

    private Station findNextStationAndAfterProcessing (
            Station beforeProcessingStation, Section nextSection,
            Consumer<Station> action, Function<Section, Station> findNextTarget
    ) {
        if (nextSection == null) {
            return beforeProcessingStation;
        }

        Station downStation = findNextTarget.apply(nextSection);

        if (action != null) {
            action.accept(downStation);
        }

        return downStation;
    }
}
