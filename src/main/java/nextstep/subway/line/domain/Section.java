package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.stream.Stream;

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

    public int getDistance() {
        return distance.getValue();
    }

    public void updateUpStation(Station station, int newDistance) {
        this.distance.validateGreaterThanNewDistance(newDistance);
        this.upStation = station;
        this.distance = new Distance(this.distance.getValue() - newDistance);
    }

    public void updateDownStation(Station station, int newDistance) {
        this.distance.validateGreaterThanNewDistance(newDistance);
        this.downStation = station;
        this.distance = new Distance(this.distance.getValue() - newDistance);
    }

    public Stream<Station> stations() {
        return Stream.of(upStation, downStation);
    }
}
