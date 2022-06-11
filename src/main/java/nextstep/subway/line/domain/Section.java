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

    private Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = Distance.from(distance);
    }

    public static Section of(Line line, Station upStation, Station downStation, int distance) {
        return new Section(line, upStation, downStation, distance);
    }

    public void updateSection(Section newSection) {
        if (upStationEquals(newSection.getUpStation())) {
            upStation = newSection.getDownStation();
            distance.subtract(newSection.getDistance());
            return;
        }
        if (downStationEquals(newSection.getDownStation())) {
            downStation = newSection.getUpStation();
            distance.subtract(newSection.getDistance());
        }
    }

    public void mergeWith(Section section) {
        downStation = section.getDownStation();
        distance.add(section.getDistance());
    }

    public boolean upStationEquals(Station station) {
        return this.upStation.equals(station);
    }

    public boolean downStationEquals(Station station) {
        return this.downStation.equals(station);
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
