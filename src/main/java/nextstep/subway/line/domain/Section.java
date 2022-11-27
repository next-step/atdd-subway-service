package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;
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

    public List<Station> stations() {
        return Arrays.asList(upStation, downStation);
    }

    public int getDistance() {
        return distance.value();
    }

    public void updateUpStation(Station station, int newDistance) {
        this.upStation = station;
        this.distance.validNewDistance(newDistance);
        this.distance.subtract(new Distance(newDistance));
    }

    public void updateDownStation(Station station, int newDistance) {
        this.downStation = station;
        this.distance.validNewDistance(newDistance);
        this.distance.subtract(new Distance(newDistance));
    }
}
