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
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id", nullable = false)
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id", nullable = false)
    private Station downStation;

    @Embedded
    private Distance distance;

    protected Section() {
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

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance.value();
    }

    public void addLine(Line line) {
        if (Objects.nonNull(this.line)) {
            return;
        }
        this.line = line;
    }

    public void updateUpStation(Station station, Distance newDistance) {
        upStation = station;
        distance = distance.minus(newDistance);
    }

    public void updateDownStation(Station station, Distance newDistance) {
        downStation = station;
        distance = distance.minus(newDistance);
    }

    public boolean isSameDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public boolean isSameUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public void repair(Section section) {
        if (Objects.isNull(section)) {
            return;
        }
        repairStation(section);
    }

    public void repairStation(Section section) {
        if (isSameUpStation(section.upStation)) {
            updateUpStation(section.downStation, section.distance);
            return;
        }

        if (isSameDownStation(section.downStation)) {
            updateDownStation(section.upStation, section.distance);
        }
    }

    public void reLocateDownStation(Section downSection) {
        downStation = downSection.downStation;
        distance = distance.plus(downSection.distance);
    }
}
