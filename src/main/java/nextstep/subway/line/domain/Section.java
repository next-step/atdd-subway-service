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

    @Embedded
    private Distance distance;

    public Section() {
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

    public void updateUpStation(Station station, Distance newDistance) {
        this.distance = this.distance.minus(newDistance);
        this.upStation = station;
    }

    public void updateDownStation(Station station, Distance newDistance) {
        this.distance = this.distance.minus(newDistance);
        this.downStation = station;
    }

    public boolean equalsUpStation(Station upStation) {
        return this.upStation == upStation;
    }

    public boolean equalsDownStation(Station station) {
        return this.downStation == station;
    }

    public boolean existUpStation() {
        return upStation != null;
    }

    public Distance plusDistance(Section section) {
        return this.distance.plus(section.distance);
    }
}
