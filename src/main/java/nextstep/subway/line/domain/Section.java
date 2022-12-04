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
        this.distance = Distance.from(distance);
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
        this.upStation = station;
        this.distance = distance.subtract(newDistance);
    }

    public void updateDownStation(Station station, Distance newDistance) {
        this.downStation = station;
        this.distance = distance.subtract(newDistance);
    }

    public List<Station> getStations() {
        return Arrays.asList(upStation, downStation);
    }

    public void rebase(Section section) {
        rebaseIfUpStationEquals(section);
        rebaseIfDownStationEquals(section);
    }

    private void rebaseIfUpStationEquals(Section section) {
        if(this.upStation.equals(section.upStation)) {
            distance = distance.subtract(section.distance);
            upStation = section.downStation;
        }
    }

    private void rebaseIfDownStationEquals(Section section) {
        if(this.downStation.equals(section.downStation)) {
            distance = distance.subtract(section.distance);
            downStation = section.upStation;
        }
    }
}
