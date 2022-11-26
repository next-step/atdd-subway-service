package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.station.domain.Station;

@Entity
public class Section {
    private static final String ERROR_MESSAGE_NOT_NULL_UP_STATION = "상행선은 필수입니다.";
    private static final String ERROR_MESSAGE_NOT_NULL_DOWN_STATION = "하행선은 필수입니다.";
    private static final String ERROR_MESSAGE_NOT_NULL_DISTANCE = "거리는 필수입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id", nullable = false)
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id", nullable = false)
    private Station downStation;

    private Distance distance;

    protected Section() {
    }

    private Section(Station upStation, Station downStation, Distance distance) {
        validEmptyStation(upStation, downStation);
        validEmptyDistance(distance);

        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validEmptyStation(Station upStation, Station downStation) {
        if (Objects.isNull(upStation)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_NULL_UP_STATION);
        }

        if (Objects.isNull(downStation)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_NULL_DOWN_STATION);
        }
    }

    private void validEmptyDistance(Distance distance) {
        if (Objects.isNull(distance)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_NULL_DISTANCE);
        }
    }

    public static Section of(Station upStation, Station downStation, Distance distance) {
        return new Section(upStation, downStation, distance);
    }

    public void toLine(Line line) {
        this.line = line;
    }

    public void updateUpStation(Section newSection) {
        this.distance = this.distance.subtract(newSection.distance);
        this.upStation = newSection.downStation;
    }

    public void updateDownStation(Section newSection) {
        this.distance = this.distance.subtract(newSection.distance);
        this.downStation = newSection.upStation;
    }

    public boolean isSameUpStation(Section section) {
        return upStation.isSameStation(section.getUpStation());
    }

    public boolean isSameDownStation(Section section) {
        return downStation.isSameStation(section.getDownStation());
    }

    public List<Station> stations() {
        List<Station> stations = new ArrayList<>();
        stations.add(upStation);
        stations.add(downStation);
        return stations;
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
}
