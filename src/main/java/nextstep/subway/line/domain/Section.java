package nextstep.subway.line.domain;

import nextstep.subway.exception.Message;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

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

    @Embedded
    private Distance distance;

    protected Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = Distance.ofValue(distance);
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
        if (distance.isSameOrShorterThan(newDistance)) {
            throw new IllegalArgumentException(Message.ERROR_INPUT_DISTANCE_SHOULD_BE_LESS_THAN_EXISTING_DISTANCE.showText());
        }
        this.upStation = station;
        distance.minus(newDistance);
    }

    public void updateDownStation(Station station, Distance newDistance) {
        if (distance.isSameOrShorterThan(newDistance)) {
            throw new IllegalArgumentException(Message.ERROR_INPUT_DISTANCE_SHOULD_BE_LESS_THAN_EXISTING_DISTANCE.showText());
        }
        this.downStation = station;
        distance.minus(newDistance);
    }

    public boolean hasDownStationSameWith(Station station) {
        return downStation.equals(station);
    }

    public boolean hasUpStationSameWith(Station station) {
        return upStation.equals(station);
    }

    public void addLine(Line line) {
        this.line = line;
        line.getSections().add(this);
    }

    public void removeConnectionWith(Section upSection) {
        this.upStation = upSection.getUpStation();
        distance.plus(upSection.getDistance());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(distance, section.distance)
                && Objects.equals(id, section.id)
                && Objects.equals(line, section.line)
                && Objects.equals(upStation, section.upStation)
                && Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line, upStation, downStation, distance);
    }
}
