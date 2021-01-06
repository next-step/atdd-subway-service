package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private Sections sections;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections = new Sections(new Section(this, upStation, downStation, distance));
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
        return sections.getStations();
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        List<Station> stations = sections.getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        // 이미 등록되어있다.
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        // 등록의 기준이 되는 station이 없어서 등록이 불가능하다.
        if (!stations.isEmpty() &&
                stations.stream().noneMatch(it -> it == upStation) &&
                stations.stream().noneMatch(it -> it == downStation)
        ) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        // 역에 대한 정보가 없는 경우에는 Section을 등록한다.
        if (stations.isEmpty()) {
            this.sections.getSections().add(new Section(this, upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted) {// 기존의 section 사이에 구간 추가, 기준은 upStation
            sections.findDownSectionBy(upStation)
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            this.sections.getSections().add(new Section(this, upStation, downStation, distance));
        } else if (isDownStationExisted) {// 기존의 section 사이에 구간 추가, 기준은 downStation
            sections.findUpSectionBy(downStation)
                    .ifPresent(it -> it.updateDownStation(upStation, distance));

            this.sections.getSections().add(new Section(this, upStation, downStation, distance));
        } else {
            throw new RuntimeException();
        }
    }

    public void deleteStation(Station station) {
        sections.deleteStation(station);
    }
}
