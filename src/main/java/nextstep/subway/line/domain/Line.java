package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

import java.util.List;

@Entity
public class Line extends BaseEntity {
    private static final String CAN_NOT_DELETE_STATION_WHEN_ONLY_ONE_SECTIONS_MESSAGE = "노선의 구간이 1개인 경우, 지하철 역을 삭제 할 수 없습니다.";
    private static final String CAN_NOT_DELETE_WHEN_NO_EXIST_STATION = "노선에 존재하지 않는 역입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private LineName name;

    @Embedded
    private LineColor color;

    @Embedded
    private final Sections sections = Sections.createEmpty();

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
        validateHasOnlyOneSectionWhenDeleteStation();
        validateNotIncludeStation(station);

        sections.removeStation(station);
    }

    public List<Station> getStations() {
        return sections.getStations();
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

    private void validateHasOnlyOneSectionWhenDeleteStation() {
        if (sections.size() == 1) {
            throw new IllegalArgumentException(CAN_NOT_DELETE_STATION_WHEN_ONLY_ONE_SECTIONS_MESSAGE);
        }
    }

    private void validateNotIncludeStation(Station station) {
        if (!sections.hasStation(station)) {
            throw new IllegalArgumentException(CAN_NOT_DELETE_WHEN_NO_EXIST_STATION);
        }
    }
}
