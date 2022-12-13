package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Arrays;
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

    private int distance;

    protected Section() {}

    public Section(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
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

    public void addStations(List<Station> stations) {
        stations.add(upStation);
        stations.add(downStation);
    }

    public void addNextStation(List<Station> stations) {
        stations.add(downStation);
    }

    public boolean isEqualUpStationNewSectionDownStation(Section section) {
        return upStation.equals(section.getDownStation());
    }

    public boolean isEqualDownStationNewSectionUpStation(Section section) {
        return downStation.equals(section.getUpStation());
    }

    public String findUpStationName() {
        return upStation.getName();
    }

    public String findDownStationName() {
        return downStation.getName();
    }

    public void belongLine(Line line) {
        this.line = line;
    }

    public List<Station> toStations() {
        return Arrays.asList(upStation, downStation);
    }

    public boolean isSameDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public boolean isSameUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public long getUpStationId() {
        return upStation.getId();
    }

    public long getDownStationId() {
        return downStation.getId();
    }

    public boolean isSameUpStation(Section section) {
        return upStation.equals(section.getUpStation());
    }

    public boolean isSameDownStation(Section section) {
        return downStation.equals(section.getDownStation());
    }
}
