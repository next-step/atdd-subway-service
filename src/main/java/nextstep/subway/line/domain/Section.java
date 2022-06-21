package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
public class Section {
    private static final String ERROR_MESSAGE_DISTANCE= "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

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

    public void update(Section newSection) {
        if (this.upStation.equals(newSection.upStation)) {
            updateUpStation(newSection.getDownStation(), newSection.getDistance());
        }
        if (this.downStation.equals(newSection.downStation)) {
            updateDownStation(newSection.getUpStation(), newSection.getDistance());
        }
    }

    public void updateUpStation(Station station, int newDistance) {
        if (this.distance <= newDistance) {
            throw new IllegalArgumentException(ERROR_MESSAGE_DISTANCE);
        }
        this.upStation = station;
        this.distance -= newDistance;
    }

    public void updateDownStation(Station station, int newDistance) {
        if (this.distance <= newDistance) {
            throw new IllegalArgumentException(ERROR_MESSAGE_DISTANCE);
        }
        this.downStation = station;
        this.distance -= newDistance;
    }

    public List<Station> getStations() {
        return Arrays.asList(upStation, downStation);
    }
}
