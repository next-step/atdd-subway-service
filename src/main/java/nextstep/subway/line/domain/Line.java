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

    public void addSection(Section section) {
        checkValidationForDuplicationSection(section.getUpStation(), section.getDownStation());
        checkValidationForValidSection(section.getUpStation(), section.getDownStation(), getStations());

        if (isEmptySection(section.getUpStation(), section.getDownStation(), section.getDistance())) {
            this.sections.add(new Section(this, section.getUpStation(), section.getDownStation(), section.getDistance()));
            return;
        }

        updateSectionStation(section.getUpStation(), section.getDownStation(), section.getDistance());
        this.sections.add(section);
    }

    public void removeStation(Station station) {
        checkValidationForEmptySections();

        removeStation(getSectionUpLineStation(station), getSectionDownLineStation(station));
    }

    private void removeStation(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section newUpLineStation = upLineStation.get();
            Section newDownLineStation = downLineStation.get();
            int newDistance = newUpLineStation.getDistance() + newDownLineStation.getDistance();
            this.sections.add(new Section(this, newUpLineStation.getUpStation(), newDownLineStation.getDownStation(), newDistance));
        }

        upLineStation.ifPresent(it -> this.sections.remove(it));
        downLineStation.ifPresent(it -> this.sections.remove(it));
    }

    private Optional<Section> getSectionUpLineStation(Station station) {
        return this.sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private Optional<Section> getSectionDownLineStation(Station station) {
        return this.sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private void checkValidationForEmptySections() {
        if (this.sections.size() <= 1) {
            throw new RuntimeException();
        }
    }

    private void updateSectionStation(Station upStation, Station downStation, int distance) {
        if (isUpStationExisted(upStation)) {
            this.sections.stream()
                    .filter(it -> it.getUpStation() == upStation)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));
        }else if (isDownStationExisted(downStation)) {
            this.sections.stream()
                    .filter(it -> it.getDownStation() == downStation)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, distance));
        }
    }

    private boolean isEmptySection(Station upStation, Station downStation, int distance) {
        if (this.sections.isEmpty()) {
            return true;
        }

        return false;
    }

    private boolean isUpStationExisted(Station upStation) {
        return getStations().stream().anyMatch(it -> it.equals(upStation));
    }

    private boolean isDownStationExisted(Station downStation) {
        return getStations().stream().anyMatch(it -> it.equals(downStation));
    }

    private void checkValidationForDuplicationSection(Station upStation, Station downStation) {
        if (isUpStationExisted(upStation) && isDownStationExisted(downStation)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void checkValidationForValidSection(Station upStation, Station downStation, List<Station> stations) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it.equals(upStation)) &&
                stations.stream().noneMatch(it -> it.equals(downStation))) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private Station findSectionFirstStation() {
        Station downStation = this.getSections().get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.getSections().stream()
                    .filter(it -> it.getDownStation().equals(finalDownStation))
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    // 리팩토링 메소드
    public List<Station> getStations() {
        if (this.getSections().isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findSectionFirstStation();
        stations.add(downStation);

        settingDownStations(downStation, stations);

        return stations;
    }

    private void settingDownStations(Station downStation, List<Station> stations) {
        while (downStation != null) {
            Station finalDownStation =  downStation;
            Optional<Section> nextLineStation = this.getSections().stream()
                    .filter(it -> it.getUpStation().equals(finalDownStation))
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }
    }
}
