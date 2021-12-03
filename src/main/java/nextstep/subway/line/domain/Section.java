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

    private int distance;

    protected Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
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

    @Deprecated
    public int getDistance() {
        return distance;
    }

    public void updateUpStation(Station station, int newDistance) {
        if (this.distance <= newDistance) {
            throw new InvalidParameterException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = station;
        this.distance -= newDistance;
    }

    public void updateDownStation(Station station, int newDistance) {
        if (this.distance <= newDistance) {
            throw new InvalidParameterException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.downStation = station;
        this.distance -= newDistance;
    }

    public boolean equalUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public boolean equalDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public boolean equalDistance(int distance) {
        return this.distance == distance;
    }

    public boolean isNextStation(Station station) {
        return this.upStation == station;
    }

    public boolean isSameUpStationAndDownStation(Section section) {
        return isSameUpStationOf(section) && isSameDownStationOf(section);
    }

    public boolean isSameUpStationOf(Section section) {
        return upStation.equals(section.upStation);
    }

    public boolean isSameDownStationOf(Section section) {
        return downStation.equals(section.downStation);
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
        addStation(section.downStation, section.distance);
    }

    public void addStation(Station station, int newDistance) {
        if (this.distance <= newDistance) {
            throw new InvalidParameterException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = station;
        this.distance -= newDistance;
    }

    public Section mergeOfNew(Section newDownSection) {
        int newDistance = distance + newDownSection.distance;
        return Section.of(line, upStation, newDownSection.downStation, newDistance);
    }
}
