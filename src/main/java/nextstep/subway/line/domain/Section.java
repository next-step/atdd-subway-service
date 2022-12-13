package nextstep.subway.line.domain;

import java.util.Objects;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import nextstep.subway.station.domain.Stations;

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

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this(line, upStation, downStation, new Distance(distance));
    }

    private StationPosition positionOfStation(Station station) {
        if (station.equals(upStation)) {
            return StationPosition.UP_STATION;
        }
        if (station.equals(downStation)) {
            return StationPosition.DOWN_STATION;
        }
        return null;
    }

    public boolean isStationOppositeOf(Station station, StationPosition stationPosition) {
        if (stationPosition == null) {
            throw new IllegalArgumentException();
        }
        return stationPosition.isOpposite(positionOfStation(station));
    }

    public Station getStationByPosition(StationPosition stationPosition) {
        if (stationPosition.isUpStation()) {
            return upStation;
        }
        if (stationPosition.isDownStation()) {
            return downStation;
        }
        return null;
    }

    public boolean isStationMatchWithPositionOf(Station station, StationPosition stationPosition) {
        return station.equals(getStationByPosition(stationPosition));
    }

    public Distance addDistanceOfSection(Section section) {
        return this.distance.add(section.distance);
    }

    public void validateSectionAddable(Stations stations) {
        checkAlreadyExist(stations);
        checkSectionAddable(stations);
    }

    private void checkAlreadyExist(Stations stations) {
        boolean isUpStationExisted = stations.stream().anyMatch(station -> station == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(station -> station == downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void checkSectionAddable(Stations stations) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
                stations.stream().noneMatch(it -> it == downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public boolean matchSamePositionStation(Section section) {
        if (this.upStation.equals(section.upStation)) {
            return true;
        }
        if (this.downStation.equals(section.downStation)) {
            return true;
        }
        return false;
    }

    public void splitSection(Section section) {
        validateDistanceOfSectionToAdd(section);
        if (this.upStation.equals(section.upStation)) {
            this.upStation = section.downStation;
        }
        if (this.downStation.equals(section.downStation)) {
            this.downStation = section.upStation;
        }
        this.distance.subtract(section.distance);
    }

    private void validateDistanceOfSectionToAdd(Section section) {
        if (compareDistance(section) >= 0) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    private int compareDistance(Section section) {
        return section.distance.compareTo(this.distance);
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

    public int getDistanceValue() {
        return distance.getDistance();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(id, section.id) && Objects.equals(line, section.line)
                && Objects.equals(upStation, section.upStation) && Objects.equals(downStation,
                section.downStation) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line, upStation, downStation, distance);
    }
}
