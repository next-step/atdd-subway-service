package nextstep.subway.line.domain;

import static nextstep.subway.exception.domain.SubwayExceptionMessage.OVER_THE_DISTANCE;

import nextstep.subway.exception.domain.SubwayException;
import nextstep.subway.generic.domain.Distance;
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

    @Column(name = "distance")
    private Distance distance;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = Distance.valueOf(distance);
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

    public void updateUpStation(Station station, int newDistanceValue) {
        final Distance newDistance = Distance.valueOf(newDistanceValue);
        if (this.distance.isLessThanOrEqualsTo(newDistance)) {
            throw new SubwayException(OVER_THE_DISTANCE);
        }
        this.upStation = station;
        this.distance = distance.minus(newDistance);
    }

    public void updateDownStation(Station station, int newDistanceValue) {
        final Distance newDistance = Distance.valueOf(newDistanceValue);
        if (this.distance.isLessThanOrEqualsTo(newDistance)) {
            throw new SubwayException(OVER_THE_DISTANCE);
        }
        this.downStation = station;
        this.distance = distance.minus(newDistance);
    }

    public boolean hasUpStation(final Station upStation) {
        return this.upStation.equals(upStation);
    }
}
