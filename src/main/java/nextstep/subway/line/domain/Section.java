package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

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
    Distance distance;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public Section(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station upStation() {
        return upStation;
    }

    public Station downStation() {
        return downStation;
    }

    public int getDistance() {
        return distance.value();
    }

    public boolean isNextOf(Section section) {
        return this.upStation.equals(section.downStation);
    }

    public List<Station> stations() {
        return Arrays.asList(upStation, downStation);
    }

    public void updateUpStation(final Section section) {
        if (this.distance.isSmallOrEqualTo(section.distance)) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = section.downStation();
        this.distance.minus(section.distance);
    }

    public void updateDownStation(final Section section) {
        if (this.distance.isSmallOrEqualTo(section.distance)) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.downStation = section.upStation();
        this.distance.minus(section.distance);
    }

    public boolean isSameDownStation(final Section section) {
        return this.downStation.equals(section.downStation);
    }

    public boolean isSameUpStation(final Section section) {
        return this.upStation.equals(section.upStation);
    }

    public void setLine(final Line line) {
        this.line = line;
    }

    public boolean isSameUpStation(final Station station) {
        return this.upStation.equals(station);
    }

    public boolean isSameDownStation(final Station station) {
        return this.downStation.equals(station);
    }
}
