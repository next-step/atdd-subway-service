package nextstep.subway.line.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.station.domain.Station;
import org.springframework.util.Assert;

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

    private Distance distance;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, Distance distance) {
        validate(upStation, downStation, distance);
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Station upStation, Station downStation, Distance distance) {
        return new Section(null, upStation, downStation, distance);
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
        return distance.getValue();
    }

    public void updateUpStation(Station station, Distance newDistance) {
        if (this.distance.lessThanOrEqual(newDistance)) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = station;
        this.distance = this.distance.subtract(newDistance);
    }

    public void updateDownStation(Station station, Distance newDistance) {
        if (this.distance.lessThanOrEqual(newDistance)) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.downStation = station;
        this.distance = this.distance.subtract(newDistance);
    }

    private void validate(Station upStation, Station downStation, Distance distance) {
        Assert.notNull(upStation, "'upStation' must not be null");
        Assert.notNull(downStation, "'downStation' must not be null");
        Assert.notNull(distance, "'distance' must not be null");
        Assert.isTrue(!upStation.equals(downStation),
            String.format("upStation(%s) and downStation(%s) must not equal",
                upStation, downStation));
    }
}
