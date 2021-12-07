package nextstep.subway.line.domain;

import java.util.*;

import javax.persistence.*;
import java.util.Objects;

import nextstep.subway.station.domain.*;

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

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Line line, Station upStation, Station downStation, Distance distance) {
        return new Section(line, upStation, downStation, distance);
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

    public int distance() {
        return distance.distance();
    }

    public void updateUpStation(Station station, Distance newDistance) {
        this.upStation = station;
        distance = distance.subtract(newDistance);
    }

    public void updateDownStation(Station station, Distance newDistance) {
        this.downStation = station;
        distance = distance.subtract(newDistance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Section))
            return false;
        Section section = (Section) o;
        return line.equals(section.line) && upStation.equals(section.upStation) && downStation.equals(
            section.downStation)
            && distance.equals(section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, upStation, downStation, distance);
    }
}
