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

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = Distance.valueOf(distance);
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

    public void updateUpStation(Section newSection) {
        this.distance = this.distance.minus(newSection.getDistance());
        this.upStation = newSection.getDownStation();
    }

    public void updateDownStation(Section newSection) {
        this.distance = this.distance.minus(newSection.getDistance());
        this.downStation = newSection.getUpStation();
    }

    public boolean isUpStationEqualsToStation(Station station) {
        return upStation == station;
    }

    public boolean isDownStationEqualsToStation(Station station) {
        return downStation == station;
    }

    public boolean isUpStationEqualsToUpStationInSection(Section sectionToAdd) {
        return sectionToAdd.isUpStationEqualsToStation(upStation);
    }

    public boolean isDownStationEqualsToDownStationInSection(Section sectionToAdd) {
        return sectionToAdd.isDownStationEqualsToStation(downStation);
    }
}
