package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

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
        this(line, upStation, downStation, new Distance(distance));
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

    public boolean isUpStationExist(List<Station> stations) {
        return stations.stream().anyMatch(it -> it.equals(upStation));
    }

    public boolean isDownStationExist(List<Station> stations) {
        return stations.stream().anyMatch(it -> it.equals(downStation));
    }

    public boolean isUpStation(Section other) {
        return upStation.equals(other.upStation);
    }

    public boolean isDownStation(Section other) {
        return downStation.equals(other.downStation);
    }

    public void updateUpStation(Section section) {
        this.upStation = section.downStation;
        this.distance = distance.subtract(section.distance);
    }

    public void updateDownStation(Section section) {
        this.downStation = section.upStation;
        this.distance = distance.subtract(section.distance);
    }

    public Distance addDistance(Section other) {
        return distance.add(other.distance);
    }
}
