package nextstep.subway.line.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nextstep.subway.station.domain.Station;

@Entity
public class Section {
    public static final Section EMPTY = new Section();

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

    private int distance;

    public Section() {}

    public Section(Line line, Station upStation, Station downStation, int distance) {
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

    public int getDistance() {
        return distance;
    }

    public int getAddFare() {
        return line.getAddFare();
    }

    public void updateUpStation(Station station, int newDistance) {
        if (this.distance <= newDistance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        upStation = station;
        distance -= newDistance;
    }

    public void updateDownStation(Station station, int newDistance) {
        if (this.distance <= newDistance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        downStation = station;
        distance -= newDistance;
    }

    public boolean isDownStationWithUp(Section beforeSection) {
        return downStation.equals(beforeSection.upStation);
    }

    public boolean isUpStationWithDown(Section beforeSection) {
        return upStation.equals(beforeSection.downStation);
    }

    public boolean sameUpStation(Section beforeSection) {
        return upStation.equals(beforeSection.upStation);
    }

    public boolean sameDownStation(Section beforeSection) {
        return downStation.equals(beforeSection.downStation);
    }

    public boolean sameUpStation(Station upStation) {
        return this.upStation.equals(upStation);
    }

    public boolean sameDownStation(Station downStation) {
        return this.downStation.equals(downStation);
    }

}
