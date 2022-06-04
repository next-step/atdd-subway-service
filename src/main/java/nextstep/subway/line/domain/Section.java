package nextstep.subway.line.domain;

import static nextstep.subway.line.enums.LineExceptionType.NEED_NARROW_DISTANCE_THAN_SECTION;

import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.station.domain.Station;

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

    private Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = Distance.from(distance);
    }

    private Section(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = Distance.from(distance);
    }

    private Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private Section(Long id, Station upStation, Station downStation, Distance distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Long id, Station upStation, Station downStation, Distance distance) {
        return new Section(id, upStation, downStation, distance);
    }

    public static Section of(Line line, Station upStation, Station downStation, int distance) {
        return new Section(line, upStation, downStation, distance);
    }

    public static Section of(Line line, Station upStation, Station downStation, Distance distance) {
        return new Section(line, upStation, downStation, distance);
    }

    public static Section of(Station upStation, Station downStation, int distance) {
        return new Section(upStation, downStation, distance);
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

    public void updateUpStation(Station station, Distance distance) {
        if (this.distance.isLessThanOrEqualsTo(distance)) {
            throw new IllegalArgumentException(NEED_NARROW_DISTANCE_THAN_SECTION.getMessage());
        }
        this.upStation = station;
        this.distance.minus(distance);
    }

    public void updateDownStation(Station station, Distance newDistance) {
        if (this.distance.isLessThanOrEqualsTo(newDistance)) {
            throw new IllegalArgumentException(NEED_NARROW_DISTANCE_THAN_SECTION.getMessage());
        }
        this.downStation = station;
        this.distance.minus(newDistance);
    }

    public void registerLine(Line line) {
        this.line = line;
    }

    public boolean isEqualsUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public boolean isEqualsDownStation(Station station) {
        return this.downStation.equals(station);
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
        return Objects.equals(getId(), section.getId()) && Objects.equals(getLine(),
            section.getLine()) && Objects.equals(getUpStation(), section.getUpStation())
            && Objects.equals(getDownStation(), section.getDownStation())
            && Objects.equals(getDistance(), section.getDistance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLine(), getUpStation(), getDownStation(), getDistance());
    }
}
