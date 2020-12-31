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

    @Embedded
    private Distance distance;

    protected Section() {
    }

    public Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this(line, upStation, downStation, new Distance(distance));
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
        Distance newDistance = upSection.distance.plus(downSection.distance);

        return new Section(upSection.line, newUpStation, newDownStation, newDistance);
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

    void updateUpStation(Station station, int newDistance) {
        this.upStation = station;
        this.distance = this.distance.minus(new Distance(newDistance));
    }

    void updateDownStation(Station station, int newDistance) {
        this.downStation = station;
        this.distance = this.distance.minus(new Distance(newDistance));
    }

    List<Station> getStations() {
        return Arrays.asList(this.upStation, this.downStation);
    }

    boolean isUpStationBelongsTo(List<Station> stations) {
        return stations.contains(this.upStation);
    }

    boolean isSameWithUpStation(Station station) {
        return this.upStation.equals(station);
    }

    boolean isSameWithDownStation(Station station) {
        return this.downStation.equals(station);
    }

    boolean isDownSameWithThatUp(Section thatSection) {
        return this.downStation.equals(thatSection.upStation);
    }
}
