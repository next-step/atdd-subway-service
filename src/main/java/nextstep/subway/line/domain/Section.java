package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

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

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
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

    public Boolean equalUpStation(Long stationId) {
        return Objects.equals(this.upStation.getId(), stationId);
    }

    public Boolean equalUpStation(Station station) {
        return Objects.equals(this.upStation, station);
    }

    public Boolean equalDownStation(Long stationId) {
        return Objects.equals(this.downStation.getId(), stationId);
    }

    public Boolean equalDownStation(Station station) {
        return Objects.equals(this.downStation, station);
    }

    public int getDistance() {
        return distance.getValue();
    }

    public void updateUpStation(Station station, Distance newDistance) {
        if (this.distance.getValue() <= newDistance.getValue()) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = station;
        this.distance = distance.substract(newDistance);
    }

    public void updateDownStation(Station station, Distance newDistance) {
        if (this.distance.getValue() <= newDistance.getValue()) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.downStation = station;
        this.distance = distance.substract(newDistance);
    }

    public boolean existDownStation() {
        return this.downStation != null;
    }

    public boolean existUpStation() {
        return this.upStation != null;
    }
}
