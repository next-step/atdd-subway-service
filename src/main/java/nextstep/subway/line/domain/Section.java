package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section {
    public static final String SECTION_DISTANCE_EXCEPTION_MESSAGE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";
    public static final int MINIMUM_DISTANCE = 0;
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

    protected Section() {
    }

    public Section(Line line, Station upStation, Station downStation, Distance distance) {
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

    public Distance getDistance() {
        return distance;
    }

    public void updateUpStation(Station station, Distance newDistance) {
        if (this.distance.compareTo(newDistance) <= MINIMUM_DISTANCE) {
            throw new RuntimeException(SECTION_DISTANCE_EXCEPTION_MESSAGE);
        }
        this.upStation = station;
        this.distance = this.distance.subtract(newDistance);
    }

    public void updateDownStation(Station station, Distance newDistance) {
        if (this.distance.compareTo(newDistance) <= 0) {
            throw new RuntimeException(SECTION_DISTANCE_EXCEPTION_MESSAGE);
        }
        this.downStation = station;
        this.distance = this.distance.subtract(newDistance);
    }
}
