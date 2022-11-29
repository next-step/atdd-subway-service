package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private Sections sections = new Sections();

    protected Line() {
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

    public void addLineStation(Station upStation, Station downStation, int distance) {
        boolean isUpStationExisted = isExisted(upStation);
        boolean isDownStationExisted = isExisted(downStation);

        validate(upStation, downStation, isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            sections.updateUpStation(upStation, downStation, distance);
        }
        if (isDownStationExisted) {
            sections.updateDownStation(upStation, downStation, distance);
        }
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void removeLineStation(Station station) {
        sections.removeLineStation(station);
    }

    private void validate(Station upStation, Station downStation, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalStateException("이미 등록된 구간 입니다.");
        }

        if (!getStations().isEmpty() && isNoneMatch(upStation) &&
            isNoneMatch(downStation)) {
            throw new IllegalStateException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isNoneMatch(Station upStation) {
        return getStations().stream()
            .noneMatch(it -> it == upStation);
    }

    private boolean isExisted(Station upStation) {
        return getStations().stream()
            .anyMatch(it -> it == upStation);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public List<Station> getStations() {
        return sections.getStations();
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
}
