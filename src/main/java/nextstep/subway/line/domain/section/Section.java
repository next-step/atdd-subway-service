package nextstep.subway.line.domain.section;

import nextstep.subway.line.domain.line.Line;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section {

    private static final Section DUMMY_SECTION = new Section();

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

    @Embedded
    private Distance distance;

    protected Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public static Section ofDummy() {
        return DUMMY_SECTION;
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

    public int getIntegerDistance() {
        return distance.getDistance();
    }

    public void updateUpStation(Station station, int newDistance) {
        this.upStation = station;
        this.distance.minus(newDistance);
    }

    public void updateDownStation(Station station, int newDistance) {
        this.downStation = station;
        this.distance.minus(newDistance);
    }

    public boolean isDummy() {
        return this == DUMMY_SECTION;
    }

}
