package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

import static nextstep.subway.line.domain.BizExceptionMessages.SECTION_UNENROLLABLE_DISTANCE;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private int distance;

    protected Section() {
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

    public boolean isSameWithUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public boolean isSameWithDownStation(Station station) {
        return this.downStation.equals(station);
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
            throw new IllegalArgumentException(SECTION_UNENROLLABLE_DISTANCE.message());
        }
    }
}
