package nextstep.subway.line.domain;

import java.util.Objects;
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

    public Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this(line, upStation, downStation, Distance.of(distance));
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

    public void updateUpStation(Station station, Distance newDistance) {
        validUpdateDistance(newDistance);
        this.upStation = station;
        distance.minusDistance(newDistance);
    }

    public void updateDownStation(Station station, Distance newDistance) {
        validUpdateDistance(newDistance);
        this.downStation = station;
        distance.minusDistance(newDistance);
    }

    public boolean isContainStation(Station station) {
        return isUpStation(station) || isDownStation(station);
    }

    public boolean isUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean isDownStation(Station station) {
        return downStation.equals(station);
    }

    private void validUpdateDistance(Distance newDistance) {
        if (distance.isEqualAndLess(newDistance)) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Section)) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(getLine(), section.getLine()) && Objects.equals(getUpStation(),
                section.getUpStation()) && Objects.equals(getDownStation(), section.getDownStation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLine(), getUpStation(), getDownStation());
    }


}

