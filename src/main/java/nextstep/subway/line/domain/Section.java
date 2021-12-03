package nextstep.subway.line.domain;

import java.security.InvalidParameterException;
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

    public static Section of(Line line, Station upStation, Station downStation, int distance) {
        return new Section(line, upStation, downStation, distance);
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public void updateUpStation(Station station, Distance distance) {
        this.distance.minus(distance);
        this.upStation = station;
    }

    public void updateDownStation(Station station, Distance distance) {
        this.distance.minus(distance);
        this.downStation = station;
    }

    public boolean isSameUpStationAndDownStation(Section section) {
        return upStation.equals(section.upStation) && downStation.equals(section.downStation);
    }

    public boolean isSameUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean isSameDownStation(Station station) {
        return downStation.equals(station);
    }

    public void updateDownStationOf(Section section) {
        updateDownStation(section.upStation, section.distance);
    }

    public void updateUpStationOf(Section section) {
        updateUpStation(section.downStation, section.distance);
    }

    public Section newOfMerge(Section newDownSection) {
        distance.plus(newDownSection.distance);
        return Section.of(line, upStation, newDownSection.downStation, distance.getDistance());
    }
}
