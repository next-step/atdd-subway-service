package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private LineName name;

    @Embedded
    private LineColor color;

    @Embedded
    private Sections sections = Sections.createEmpty();

    protected Line() {}

    private Line(String name, String color) {
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
    }

    private Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
        sections.add(Section.of(this, upStation, downStation, Distance.from(distance)));
    }

    public static Line createEmpty() {
        return new Line();
    }

    public static Line of(String name, String color) {
        return new Line(name, color);
    }

    public static Line of(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
    }

    public void addSection(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it.equals(section.getUpStation()));
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it.equals(section.getDownStation()));

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it.equals(section.getUpStation())) &&
            stations.stream().noneMatch(it -> it.equals(section.getDownStation()))) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            sections.add(section);
            return;
        }

        if (isUpStationExisted) {
            sections.findByUpStation(section.getUpStation())
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            sections.add(section);
        } else if (isDownStationExisted) {
            sections.findByDownStation(section.getDownStation())
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

            sections.add(section);
        } else {
            throw new RuntimeException();
        }
    }

    public void removeStation(Station station) {
        // 중간역일 때 처리
        Optional<Section> upLineStation = sections.findByUpStation(station);
        Optional<Section> downLineStation = sections.findByDownStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            Distance newDistance = upLineStation.get()
                                                .getDistance()
                                                .plus(downLineStation.get()
                                                                     .getDistance());
            sections.add(Section.of(this, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
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
            Optional<Section> nextLineStation = sections.findByUpStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public Long getId() {
        return id;
    }

    public LineName getName() {
        return name;
    }

    public LineColor getColor() {
        return color;
    }

    private Station findUpStation() {
        Station downStation = sections.findFirstSection().getUpStation();
        while (downStation != null) {
            Optional<Section> nextLineStation = sections.findByDownStation(downStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }
}
