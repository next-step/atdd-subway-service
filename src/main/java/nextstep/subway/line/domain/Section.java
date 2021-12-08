package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

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
        this(line, upStation, downStation, new Distance(distance));
    }

    public Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
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

    public int getDistanceValue() {
        return distance.distance();
    }

    public void updateUpStation(Station station, Distance newDistance) {
        checkInputDistanceThanDistanceBetweenStations(this.distance, newDistance);
        this.upStation = station;
        this.distance = this.distance.minus(newDistance);
    }

    public void updateDownStation(Station station, Distance newDistance) {
        checkInputDistanceThanDistanceBetweenStations(this.distance, newDistance);
        this.downStation = station;
        this.distance = this.distance.minus(newDistance);
    }

    public List<Station> getStations() {
        return Arrays.asList(upStation, downStation);
    }

    private void checkInputDistanceThanDistanceBetweenStations(Distance distance, Distance newDistance) {
        if (distance.distance() <= newDistance.distance()) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }
}
