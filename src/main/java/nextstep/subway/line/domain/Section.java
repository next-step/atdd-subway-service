package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
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

    public void updateUpStation(Section section) {
        checkValidationDistance(section.distance);
        this.upStation = section.downStation;
        this.distance -= section.distance;
    }

    public void updateDownStation(Section section) {
        checkValidationDistance(section.distance);
        this.downStation = section.upStation;
        this.distance -= section.distance;
    }

    private void checkValidationDistance(int newDistance) {
        if (this.distance <= newDistance) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    public Section merge(Section section) {
        this.distance += section.distance;
        return new Section(this.line, section.upStation, this.downStation, this.distance);
    }

    public boolean equalsById(long source, long target) {
        return (upStation.isSameId(source) && downStation.isSameId(target)) ||
                (upStation.isSameId(target) && downStation.isSameId(source));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return (upStation.equals(section.upStation) &&
                downStation.equals(section.downStation)) ||
                (upStation.equals(section.downStation) &&
                        downStation.equals(section.upStation));
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation);
    }
}
