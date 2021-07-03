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

    @Embedded
    private SectionDistance distance;

    /**
     * 생성자
     */
    protected Section() {
    }

    public Section(Long id, Line line, Station upStation, Station downStation, int distance) {
        this(line, upStation, downStation, distance);
        this.id = id;
    }

    public Section(Long id, Station upStation, Station downStation, int distance) {
        this(upStation, downStation, distance);
        this.id = id;
    }

    public Section(Station upStation, Station downStation, int distance) {
        verifyAvailableStations(upStation, downStation);
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new SectionDistance(distance);
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this(upStation, downStation, distance);
        verifyAvailableLine(line);
        this.line = line;
    }

    private static void verifyAvailableStations(Station upStation, Station downStation) {
        if (Objects.isNull(upStation) || Objects.isNull(downStation)) {
            throw new IllegalArgumentException("구간 생성정보(역)가 충분하지 않습니다.");
        }
    }

    private static void verifyAvailableLine(Line line) {
        if (Objects.isNull(line)) {
            throw new IllegalArgumentException("구간 생성정보(노선)가 충분하지 않습니다.");
        }
    }

    /**
     * 비즈니스 메소드
     */
    public void updateUpStation(Station station, int newDistance) {
        distance.updateDistance(newDistance);
        this.upStation = station;
    }

    public void updateDownStation(Station station, int newDistance) {
        distance.updateDistance(newDistance);
        this.downStation = station;
    }

    public void setLine(Line line) {
        verifyAvailableLine(line);
        this.line = line;
    }

    /**
     * 기타 메소드
     */
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

    public SectionDistance getDistance() {
        return distance;
    }

    public double getWeight() { return distance.getWeight(); }
}
