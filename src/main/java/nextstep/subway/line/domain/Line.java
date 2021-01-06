package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
        addNewSection(upStation, downStation, distance);
    }

    public Line(Long id, String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color, upStation, downStation, distance);
        this.id = id;
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

    public Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getSectionByDownStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getSectionByUpStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public void addLineStation(Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();

        verifyAddLineStation(upStation, downStation, stations);

        if (addLineStationIfEmpty(upStation, downStation, distance, stations))
            return;

        updateUpStation(upStation, downStation, distance, stations);
        updateDownStation(upStation, downStation, distance, stations);

        addNewSection(upStation, downStation, distance);
    }

    public void removeLineStation(Station station) {
        verifyCannotRemove();

        Optional<Section> upLineStation = getSectionByUpStation(station);
        Optional<Section> downLineStation = getSectionByDownStation(station);

        replaceSectionByRemove(upLineStation, downLineStation);
        removeSection(upLineStation, downLineStation);
    }

    private void verifyCannotRemove() {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }
    }

    private void replaceSectionByRemove(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(this, newUpStation, newDownStation, newDistance));
        }
    }

    private void removeSection(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        upLineStation.ifPresent(it -> getSections().remove(it));
        downLineStation.ifPresent(it -> getSections().remove(it));
    }

    private Optional<Section> getSectionByUpStation(Station station) {
        return getSection(it -> it.getUpStation() == station);
    }

    private Optional<Section> getSectionByDownStation(Station station) {
        return getSection(it -> it.getDownStation() == station);
    }

    private Optional<Section> getSection(Predicate<Section> sectionPredicate) {
        return sections.stream()
                .filter(sectionPredicate)
                .findFirst();
    }

    private void verifyAddLineStation(Station upStation, Station downStation, List<Station> stations) {
        boolean isUpStationExisted = isStationExisted(upStation, stations);
        boolean isDownStationExisted = isStationExisted(downStation, stations);

        verifyAlreadyExistSection(isUpStationExisted, isDownStationExisted);
        verifyNotMatchStations(isUpStationExisted, isDownStationExisted);
    }

    private void verifyNotMatchStations(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void verifyAlreadyExistSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void updateUpStation(Station upStation, Station downStation, int distance, List<Station> stations) {
        if (isStationExisted(upStation, stations))
            updateStation(it -> it.getUpStation() == upStation, it -> it.updateUpStation(downStation, distance));
    }

    private void updateDownStation(Station upStation, Station downStation, int distance, List<Station> stations) {
        if (isStationExisted(downStation, stations))
            updateStation(it -> it.getDownStation() == downStation, it -> it.updateDownStation(upStation, distance));
    }

    private void updateStation(Predicate<Section> sectionPredicate, Consumer<Section> sectionConsumer) {
        sections.stream()
                .filter(sectionPredicate)
                .findFirst()
                .ifPresent(sectionConsumer);
    }

    private boolean addLineStationIfEmpty(Station upStation, Station downStation, int distance, List<Station> stations) {
        if (stations.isEmpty()) {
            addNewSection(upStation, downStation, distance);
            return true;
        }
        return false;
    }

    private boolean isStationExisted(Station station, List<Station> stations) {
        return stations.stream().anyMatch(it -> it == station);
    }

    private void addNewSection(Station upStation, Station downStation, int distance) {
        sections.add(new Section(this, upStation, downStation, distance));
    }
}
