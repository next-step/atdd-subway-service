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

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = Distance.of(distance);
    }

    public static Section of(Station upStation, Station downStation, int distance) {
        return new Section(null, upStation, downStation, distance);
    }

    public void toLine(Line line) {
        this.line = line;
    }

    public Section newOfMerge(Section section) {
        distance.plus(section.distance);
        return new Section(line, upStation, section.downStation, distance.getDistance());
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Integer getDistance() {
        return distance.getDistance();
    }

    public void relocationUpStation(Section newSection) {
        this.distance.minus(newSection.getDistance());
        this.upStation = newSection.downStation;
    }

    public boolean isSameUpStationAndDownStation(Section section) {
        return isSameUpStation(section.upStation) && isSameDownStation(section.downStation);
    }

    public boolean isSameUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean isSameDownStation(Station station) {
        return downStation.equals(station);
    }
}
