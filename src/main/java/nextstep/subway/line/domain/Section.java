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

    protected Section() {
    }

    private Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Line line, Station upStation, Station downStation, Distance distance) {
        return new Section(line, upStation, downStation, distance);
    }

    public void updateSection(Section section) {
        if (this.upStationEqualTo(section.getUpStation())) {
            this.upStation = section.getDownStation();
        }

        if (this.downStationEqualTo(section.getDownStation())) {
            this.downStation = section.getUpStation();
        }

        this.distance.checkAddSection(section);
        this.distance = this.distance.minus(section.getDistance());
    }

    public boolean downStationEqualTo(Station station) {
        return downStation.equals(station);
    }

    public boolean upStationEqualTo(Station station) {
        return upStation.equals(station);
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
