package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class Section {

    public static final Section EMPTY = new Section();

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

    public Section() {
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
        this.distance = this.distance.getRemainedDistance(newDistance);
        this.upStation = station;
    }

    public void updateDownStation(Station station, Distance newDistance) {
        this.distance = this.distance.getRemainedDistance(newDistance);
        this.downStation = station;
    }

    public boolean isExists() {
        return !Objects.equals(this, Section.EMPTY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(id, section.id) && Objects.equals(line, section.line) && Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line, upStation, downStation, distance);
    }
}
