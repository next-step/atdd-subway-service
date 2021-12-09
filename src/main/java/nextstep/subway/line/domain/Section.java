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

    Section(Station downStation) {
        this.downStation = downStation;
    }

    Section(int distance) {
        this.distance = new Distance(distance);
    }

    Section(Station upStation, Station downStation) {
        this.upStation = upStation;
        this.downStation = downStation;
    }

    Section(Station upStation, Station downStation, int distance) {
        this(upStation, downStation);
        this.distance = new Distance(distance);
    }

    public Section(Line line, Station upStation, Station downStation) {
        this(upStation, downStation);
        this.line = line;
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this(line, upStation, downStation);
        this.distance = new Distance(distance);
    }

    public Section(Line line, Station upStation, Station downStation, Distance distance) {
        this(line, upStation, downStation);
        this.distance = distance;
    }

    public static Section of(Line line, Station upStation, Station downStation, int distance) {
        return new Section(line, upStation, downStation, distance);
    }

    public static Section of(Line line, Station upStation, Station downStation, Distance distance) {
        return new Section(line, upStation, downStation, distance);
    }

    public boolean isEqualsDownStation(Station station) {
        return this.downStation == station;
    }

    public boolean isEqualsUpStation(Station station) {
        return this.upStation == station;
    }

    public boolean isEqualsUpStation(Section section) {
        return this.upStation == section.upStation;
    }

    public void updateUpStation(Section section) {
        this.upStation = section.downStation;
        this.distance = this.distance.minus(section.distance);
    }

    public boolean isEqualsDownStation(Section section) {
        return this.downStation == section.downStation;
    }

    public void updateDownStation(Section section) {
        this.downStation = section.upStation;
        this.distance = this.distance.minus(section.distance);
    }

    public Distance plusDistance(Section section) {
        return this.distance.plus(section.distance);
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
