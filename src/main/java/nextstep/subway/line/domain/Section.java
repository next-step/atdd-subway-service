package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;
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

    protected Section() {}

    private Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Line line, Station upStation, Station downStation, int distance) {
        return new Section(line, upStation, downStation, distance);
    }

    public static Section of(Station upStation, Station downStation, int distance) {
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
        return distance;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public void updateUpStation(Station station, int newDistance) {
        validateDistance(newDistance);
        this.upStation = station;
        this.distance -= newDistance;
    }

    public void updateDownStation(Station station, int newDistance) {
        validateDistance(newDistance);
        this.downStation = station;
        this.distance -= newDistance;
    }

    private void validateDistance(int newDistance) {
        if (this.distance <= newDistance) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    public void update(Station upStation, Station downStation, int newDistance) {
        if (this.upStation == upStation) {
            updateUpStation(downStation, newDistance);
        }
        if (this.downStation == downStation) {
            updateDownStation(upStation, newDistance);
        }
    }

    public List<Station> getStations() {
        return Arrays.asList(upStation, downStation);
    }
}
