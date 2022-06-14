package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

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

    public void updateUpStationAndDistanceFor(Section newSection) {
        if (!hasSameUpStationAs(newSection)) {
            throw new IllegalArgumentException("상행 역이 일치하는 구간을 입력해주세요.");
        }

        if (this.distance <= newSection.distance) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = newSection.downStation;
        this.distance -= newSection.distance;
    }

    public void updateDownStationAndDistanceFor(Section newSection) {
        if (!hasSameDownStationAs(newSection)) {
            throw new IllegalArgumentException("하행 역이 일치하는 구간을 입력해주세요.");
        }

        if (this.distance <= newSection.distance) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.downStation = newSection.upStation;
        this.distance -= newSection.distance;
    }

    public Section merge(Section other) {
        if (downStation.equals(other.upStation)) {
            return new Section(line, upStation, other.downStation, distance + other.distance);
        }

        if (upStation.equals(other.downStation)) {
            return new Section(line, other.upStation, downStation, distance + other.distance);
        }

        throw new IllegalArgumentException("구간을 합칠 수 없습니다.");
    }

    public boolean isPrevSectionOf(Section other) {
        return downStation.equals(other.upStation);
    }

    public boolean isNextSectionOf(Section other) {
        return upStation.equals(other.downStation);
    }

    public boolean hasUpStationSameAs(Station station) {
        return upStation.equals(station);
    }

    public boolean hasSameUpStationAs(Section other) {
        return upStation.equals(other.upStation);
    }

    public boolean hasDownStationSameAs(Station station) {
        return downStation.equals(station);
    }

    public boolean hasSameDownStationAs(Section other) {
        return downStation.equals(other.downStation);
    }

    public boolean hasExactlySameStationsAs(Section newSection) {
        return upStation.equals(newSection.upStation) && downStation.equals(newSection.downStation);
    }

    public boolean hasAtLeastOneSameStationOf(Section other) {
        return upStation.equals(other.upStation) ||
                upStation.equals(other.downStation) ||
                downStation.equals(other.upStation) ||
                downStation.equals(other.downStation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return distance == section.distance &&
                Objects.equals(id, section.id) &&
                Objects.equals(upStation, section.upStation) &&
                Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStation, downStation, distance);
    }
}
