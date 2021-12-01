package nextstep.subway.line.domain;

import nextstep.subway.common.exception.SectionNotCreateException;
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

    @Embedded
    private Distance distance;

    protected Section() {
    }

    public Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void updateUpStation(Station station, Distance newDistance) {
        if (this.distance.isLessThanOrEqualTo(newDistance)) {
            throw new SectionNotCreateException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = station;
        this.distance = Distance.valueOf(DistanceType.MINUS, this.distance, newDistance);
    }

    public void updateDownStation(Station station, Distance newDistance) {
        if (this.distance.isLessThanOrEqualTo(newDistance)) {
            throw new SectionNotCreateException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.downStation = station;
        this.distance = Distance.valueOf(DistanceType.MINUS, this.distance, newDistance);
    }

    public boolean downStationEqualTo(Station station) {
        return downStation.equals(station);
    }

    public boolean upStationEqualTo(Station station) {
        return upStation.equals(station);
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

    public Distance getDistance() {
        return distance;
    }
}
