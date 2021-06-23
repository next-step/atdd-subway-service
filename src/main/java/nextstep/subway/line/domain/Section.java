package nextstep.subway.line.domain;

import nextstep.subway.line.exception.InvalidSectionException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private int distance;

    public Section() {
    }

    Section(Long id, Line line, Station upStation, Station downStation, int distance) {
        this.id = id;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this(null, line, upStation, downStation, distance);
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isLongerThan(Section section) {
        return this.distance > section.distance;
    }

    public boolean matchesOnlyOneEndOf(Section section) {
        return upStation == section.upStation
            ^ downStation == section.downStation;
    }

    public void updateUpStation(Station station, int newDistance) {
        if (this.distance <= newDistance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = station;
        this.distance -= newDistance;
    }

    public void updateDownStation(Station station, int newDistance) {
        if (this.distance <= newDistance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.downStation = station;
        this.distance -= newDistance;
    }

    public void updateSection(Section section) {
        checkSection(section);

        if (this.upStation == section.getUpStation()) {
            this.upStation = section.getDownStation();
        }

        if (this.downStation == section.getDownStation()) {
            this.downStation = section.getUpStation();
        }

        this.distance -= section.getDistance();
    }

    private void checkSection(Section section) {
        if (!this.matchesOnlyOneEndOf(section)) {
            throw new InvalidSectionException("하나의 종단점만 일치해야 합니다.");
        }

        if (!this.isLongerThan(section)) {
            throw new InvalidSectionException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요.");
        }
    }
}
