package nextstep.subway.line.domain;

import nextstep.subway.line.domain.exceptions.InvalidMergeSectionException;
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

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section mergeByTwoSections(Section upSection, Section downSection) {
        if (!upSection.line.isSameName(downSection.line)) {
            throw new InvalidMergeSectionException("서로 다른 노선에 있는 구간끼리 병합할 수 없습니다.");
        }

        if (upSection.upStation != downSection.downStation) {
            throw new InvalidMergeSectionException("겹치는 역이 없는 구간끼리 병합할 수 없습니다.");
        }

        Station newUpStation = downSection.upStation;
        Station newDownStation = upSection.downStation;
        int newDistance = upSection.distance + downSection.distance;

        return new Section(upSection.line, newUpStation, newDownStation, newDistance);
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
        if (this.distance < newDistance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = station;
        this.distance -= newDistance;
    }

    public void updateDownStation(Station station, int newDistance) {
        if (this.distance < newDistance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.downStation = station;
        this.distance -= newDistance;
    }

    public List<Station> getStations() {
        return Arrays.asList(this.upStation, this.downStation);
    }

    public boolean isUpStationBelongsTo(List<Station> stations) {
        return stations.contains(this.upStation);
    }

    public boolean isSameWithUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public boolean isSameWithDownStation(Station station) {
        return this.downStation.equals(station);
    }
}
