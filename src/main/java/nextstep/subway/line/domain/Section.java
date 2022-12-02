package nextstep.subway.line.domain;

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

    @Embedded
    private TempDistance tempDistance;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.tempDistance = new TempDistance(distance);
    }

    public Section(Line line, Station upStation, Station downStation, TempDistance tempDistance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.tempDistance = tempDistance;
        this.distance = tempDistance.getTempDistance();
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

    public void updateUpStation(Station station, TempDistance newDistance) {
        this.upStation = station;
        this.tempDistance = this.tempDistance.subtract(newDistance);
    }

    public void updateDownStation(Station station, TempDistance newDistance) {
        this.downStation = station;
        this.tempDistance = this.tempDistance.subtract(newDistance);
    }

    public TempDistance getTempDistance() {
        return tempDistance;
    }

}
