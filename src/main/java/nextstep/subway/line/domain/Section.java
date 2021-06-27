package nextstep.subway.line.domain;

import nextstep.subway.line.collection.Distance;
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

    public Section(Station upStation, Station downStation, Distance distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private Distance measureOldDistance(Distance oldDistance, boolean isConnect) {
        return this.distance.measureOldDistance(oldDistance, isConnect);
    }

    public boolean isSameUpStationOfSection(Section newSection) {
        if (newSection.isSameUpStation(this.upStation)) {
            this.distance = newSection.measureOldDistance(this.distance, false);
            this.upStation = newSection.getDownStation();
        }
        return false;
    }

    public boolean isSameUpStation(Station oldUpStation) {
        return this.upStation.equals(oldUpStation);
    }

    public boolean isSameDownStationOfSection(Section newSection) {
        if (newSection.isSameDownStation(this.downStation)) {
            this.distance = newSection.measureOldDistance(this.distance, false);
            this.downStation = newSection.getUpStation();
        }
        return false;
    }

    public boolean isSameDownStation(Station downStation) {
        return this.downStation.equals(downStation);
    }

    public void isSameSection(Section newSection) {
        if (newSection.isSameUpStation(this.upStation) && newSection.isSameDownStation(this.downStation)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    public boolean isUpFinalSection(Section newSection) {
        return newSection.isSameDownStation(this.upStation);
    }

    public boolean isDownFinalSection(Section newSection) {
        return newSection.isSameUpStation(this.downStation);
    }

    public boolean getSectionSameDownStation(Long stationId) {
        return this.downStation.isThisStation(stationId);
    }

    public boolean getSectionSameUpStation(Long stationId) {
        return this.upStation.isThisStation(stationId);
    }

    public void connectNewSection(Section downSection) {
        this.downStation = downSection.getDownStation();
        this.distance = downSection.getDistance().measureOldDistance(this.distance, true);
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

    public void setLine(Line line) {
        this.line = line;
    }
}
