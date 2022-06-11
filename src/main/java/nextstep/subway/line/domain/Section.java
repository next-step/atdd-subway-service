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

    public List<Station> findStations() {
        return Arrays.asList(upStation, downStation);
    }

    public void update(Section newSection) {
        if (isEqualsUpStation(newSection.getUpStation())) {
            this.upStation = newSection.downStation;
            updateDistance(newSection);
        }
        if (isEqualsDownStation(newSection.getDownStation())) {
            this.downStation = newSection.upStation;
            updateDistance(newSection);
        }
    }

    private boolean isEqualsUpStation(Station station) {
        return this.upStation.equals(station);
    }

    private boolean isEqualsDownStation(Station station) {
        return this.downStation.equals(station);
    }

    private void updateDistance(Section newSection) {
        if (this.distance <= newSection.distance) {
            throw new IllegalArgumentException("기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없습니다.");
        }
        this.distance -= newSection.distance;
    }
}
