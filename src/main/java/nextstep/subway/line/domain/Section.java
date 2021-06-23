package nextstep.subway.line.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nextstep.subway.station.domain.Station;

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

    public boolean isUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public boolean isDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public boolean isDownStation(Section section) {
        return this.downStation.equals(section.downStation);
    }

    public boolean isUpStation(Section section) {
        return this.upStation.equals(section.upStation);
    }

    public boolean isExistsUpStation(Section section) {
        return upStation.equals(section.upStation) || downStation.equals(section.upStation);
    }

    public boolean isExistsDownStation(Section section) {
        return upStation.equals(section.downStation) || downStation.equals(section.downStation);
    }

    public boolean isExistsStation(Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    public Distance getDistance() {
        return distance;
    }

    public Distance addDistance(Section downSection) {
        return distance.add(downSection.distance);
    }

    public void connectUpStation(Section newSection) {
        this.upStation = newSection.downStation;
        this.distance = distance.diff(newSection.distance);
    }

    public void connectDownStation(Section newSection) {
        this.downStation = newSection.upStation;
        this.distance = distance.diff(newSection.distance);
    }

    public void updateUpStation(Section newSection) {
        this.upStation = newSection.downStation;
        this.distance = distance.diff(newSection.distance);
    }

    public void updateDownStation(Section newSection) {
        this.downStation = newSection.upStation;
        this.distance = distance.diff(newSection.distance);
    }
}
