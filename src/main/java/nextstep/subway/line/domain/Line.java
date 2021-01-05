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
        sections.addSection(new Section(this, upStation, downStation, distance));
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

    public List<Station> getStations() {
        if (Objects.isNull(this.sections)) {
            return Collections.emptyList();
        }
        return this.sections.getStations();
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);
        boolean isMatchedStationNotExisted = stations.stream().noneMatch(it -> it == upStation) &&
                stations.stream().noneMatch(it -> it == downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && isMatchedStationNotExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            sections.addSection(new Section(this, upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted) {
            sections.findSectionWithUpStation(upStation)
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            sections.addSection(new Section(this, upStation, downStation, distance));
        }

        if (isDownStationExisted) {
            sections.findSectionWithDownStation(downStation)
                    .ifPresent(it -> it.updateDownStation(upStation, distance));

            sections.addSection(new Section(this, upStation, downStation, distance));
        }
    }

    public void removeStation(Station station) {
        if (sections.isNotRemovable()) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = sections.findSectionWithUpStation(station);
        Optional<Section> downLineStation = sections.findSectionWithDownStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.addSection(new Section(this, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(sections::removeSection);
        downLineStation.ifPresent(sections::removeSection);
    }

}
