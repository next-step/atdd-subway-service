package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import static java.util.Arrays.*;
import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
        addSection(upStation, downStation, distance);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        sections.add(new Section(this, upStation, downStation, distance));
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
            return emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station firstStation = findUpStation();
        stations.add(firstStation);

        addDownStation(stations, firstStation);

        return stations;
    }

    private void addDownStation(List<Station> stations, Station downStation) {
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getSection(it -> it.getUpStation() == finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getSection(it -> it.getDownStation() == finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    private Optional<Section> getSection(Predicate<Section> sectionPredicate) {
        return sections.stream()
                .filter(sectionPredicate)
                .findFirst();
    }

    public void addLineStation(Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();
        checkSectionForAdding(stations, upStation, downStation);

        if (stations.isEmpty()) {
            addSection(upStation, downStation, distance);
            return;
        }

        if (stations.contains(upStation)) {
            linkUpSection(upStation, downStation, distance);
            return;
        } else if (stations.contains(downStation)) {
            linkDownSection(upStation, downStation, distance);
            return;
        }

        throw new RuntimeException("구간을 연결할 수 없습니다");
    }

    private void checkSectionForAdding(List<Station> stations, Station upStation, Station downStation) {
        if (stations.containsAll(asList(upStation, downStation))) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
        if (!stations.isEmpty()
                && !stations.contains(upStation)
                && !stations.contains(downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void linkUpSection(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
        addSection(upStation, downStation, distance);
    }

    private void linkDownSection(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
        addSection(upStation, downStation, distance);
    }

    public void removeLineStation(Station station) {
        checkSectionForRemoving();

        Optional<Section> upLineStation = getUpLineStation(station);
        Optional<Section> downLineStation = getDownLineStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            addSection(newUpStation, newDownStation, mergeDistance(upLineStation.get().getDistance(), downLineStation.get().getDistance()));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void checkSectionForRemoving() {
        if (sections.size() <= 1) {
            throw new RuntimeException("구간을 삭제할 수 없습니다");
        }
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

    private int mergeDistance(int upDistance, int downDistance) {
        return upDistance + downDistance;
    }
}
