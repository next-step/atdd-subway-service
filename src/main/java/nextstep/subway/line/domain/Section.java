package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidDistanceException;
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

    private Distance distance;

    public Section() {
    }

    public Section(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public Section(Station upStation, Station downStation, Distance distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public void updateLine(Line line) {
        this.line = line;
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

    public void updateUpStation(Station station, Distance newDistance) {
        updateUpStation(station, newDistance.getDistance());
    }

    public void updateUpStation(Station station, int newDistance) {
        if (this.distance.getDistance() <= newDistance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = station;
        this.distance = this.distance.minusDistance(newDistance);
    }

    public void updateDownStation(Station station, Distance newDistance) {
        updateDownStation(station, newDistance.getDistance());
    }

    public void updateDownStation(Station station, int newDistance) {
        if (this.distance.getDistance() <= newDistance) {
            throw new InvalidDistanceException("기존 구간보다 짧은 구간을 가진 지하철 역만 추가할 수 있습니다.");
        }
        this.downStation = station;
        this.distance = this.distance.minusDistance(newDistance);
    }

    public boolean containsStation(Station station) {
        return station.equals(getUpStation())
                || station.equals(getDownStation());
    }

    public Section connectSection(Section target) {
        Distance plusDistance = getDistance().plusDistance(target.getDistance());
        return new Section(upStation, target.getDownStation(), plusDistance);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
    }
}
