package nextstep.subway.line.domain;

import nextstep.subway.exception.NotValidDataException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

import static nextstep.subway.exception.type.ValidExceptionType.NOT_SHORT_VALID_DISTANCE;

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

    private Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Line line, Station upStation, Station downStation, Distance distance) {
        return new Section(line, upStation, downStation, distance);
    }

    public void updateUpStation(Station station, int newDistance) {
        validCheckIsOverDistance(newDistance);

        this.upStation = station;
        this.distance = this.distance.minus(newDistance);
    }

    public void updateDownStation(Station station, int newDistance) {
        validCheckIsOverDistance(newDistance);

        this.downStation = station;
        this.distance.minus(newDistance);
    }

    private void validCheckIsOverDistance(int checkTargetDistance) {
        if (distance.isOverDistance(checkTargetDistance)) {
            throw new NotValidDataException(NOT_SHORT_VALID_DISTANCE.getMessage());
        }
    }

    public Distance plusDistance(Section section) {
        return this.distance.plus(section.distance);
    }

    public boolean isSameDownStation(Station downStation) {
        return this.downStation.equals(downStation);
    }

    public boolean isSameUpStation(Station upStation) {
        return this.upStation.equals(upStation);
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
        return distance.getDistance();
    }
}
