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

    private int distance;

    public Section() {
    }

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

    public void updateUpStation(Station station, int newDistance) {
        if (this.distance <= newDistance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = station;
        this.distance -= newDistance;
    }

    public void updateDownStation(Station station, int newDistance) {
        if (this.distance <= newDistance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.downStation = station;
        this.distance -= newDistance;
    }

    public boolean isUpStationEquals(Station station) {
        return this.upStation == station;
    }

    public boolean isDownStationEquals(Station station) {
        return this.downStation == station;
    }

    public boolean containsStation(Station station) {
        return this.upStation == station || this.downStation == station
                ;
    }

    public boolean containsSameDownStation(Section section) {
        return this.upStation == section.upStation;
    }

    public boolean containsSameUpStation(Section section) {
        return this.downStation == section.downStation;
    }

    public boolean containsByUpStation(Section section) {
        return this.upStation == section.upStation || this.downStation == section.upStation;
    }

    public boolean containsByDownStation(Section section) {
        return this.upStation == section.downStation || this.downStation == section.downStation;
    }
}
