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

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        getSections().add(new Section(this, upStation, downStation, distance));
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
        return sections.getSections();
    }
    
    public void addSection(Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it.equals(upStation));
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it.equals(downStation));

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it.equals(upStation)) &&
                stations.stream().noneMatch(it -> it.equals(downStation))) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            getSections().add(new Section(this, upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted) {
            getSections().stream()
                    .filter(it -> it.getUpStation().equals(upStation))
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            getSections().add(new Section(this, upStation, downStation, distance));
        } else if (isDownStationExisted) {
            getSections().stream()
                    .filter(it -> it.getDownStation().equals(downStation))
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, distance));

            getSections().add(new Section(this, upStation, downStation, distance));
        } else {
            throw new RuntimeException();
        }
    }

    public void removeStation(Station station) {
        sections.removeStation(station,this);
    }

    public Station findUpStation() {
        return sections.findUpStation();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
