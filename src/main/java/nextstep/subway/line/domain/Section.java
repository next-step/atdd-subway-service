package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section implements Comparable<Section>{
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

    public int getDistance() {
        return distance.get();
    }

    public List<Station> getUpDownStations() {
        return Arrays.asList(upStation, downStation);
    }

    public void updateUpStation(Station station, int newDistance) {
        update(station, this.downStation, distance.minus(newDistance));
    }

    public void updateDownStation(Station station, int newDistance) {
        update(this.upStation, station, distance.minus(newDistance));
    }

    private void update(Station upStation, Station downStation, Distance distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    @Override
    public int compareTo(Section o) {
        if (this.upStation.equals(o.downStation)) {
            return 1;
        }

        if (this.downStation.equals(o.upStation)) {
            return -1;
        }

        return 0;
    }
}
