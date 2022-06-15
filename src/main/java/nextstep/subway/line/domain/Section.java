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
    private Distance distance;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this(line, upStation, downStation, new Distance(distance));
    }

    public Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        updateLineBy(line);
        updateDownStationBy(downStation);
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

    public boolean isMatchUpStation(final Station source) {
        return Objects.equals(upStation, source);
    }

    public boolean isMatchDownStation(final Station source) {
        return Objects.equals(downStation, source);
    }

    public void updateUpStation(Station station, Distance newDistance) {
        this.upStation = station;
        this.distance.minus(newDistance);
    }

    public void updateDownStation(Station station, Distance newDistance) {
        this.downStation = station;
        this.distance.minus(newDistance);
    }

    public void updateLineBy(final Line line) {
        this.line = line;
        if (!line.getSections().isContains(this)) {
            line.addSection(this);
        }
    }

    private void updateDownStationBy(final Station downStation) {
        if (Objects.nonNull(this.downStation) && !Objects.equals(this.downStation, downStation)) {
            this.downStation.getSections().remove(this);
        }
        if (!downStation.getSections().contains(this)) {
            downStation.getSections().add(this);
        }
        this.downStation = downStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return distance == section.distance && Objects.equals(id, section.id) && Objects.equals(line, section.line) && Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line, upStation, downStation, distance);
    }
}
