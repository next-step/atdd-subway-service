package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidSectionDistanceException;
import nextstep.subway.message.ExceptionMessage;
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

    private int distance;

    public Section() {
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

    public void updateUpStation(Section section) {
        checkValidDistance(section.distance);
        this.upStation = section.downStation;
        this.distance -= section.distance;
    }

    public void updateDownStation(Section section) {
        checkValidDistance(section.distance);
        this.downStation = section.upStation;
        this.distance -= section.distance;
    }

    private void checkValidDistance(int newDistance) {
        if (this.distance <= newDistance) {
            throw new InvalidSectionDistanceException(ExceptionMessage.INVALID_SECTION_DISTANCE);
        }
    }

    public boolean hasAnyMatchedStation(Section section) {
        return this.hasAnyMatchedThisUpStation(section) || this.hasAnyMatchedThisDownStation(section);
    }

    public boolean hasAnyMatchedThisUpStation(Section section) {
        return this.upStation.equals(section.upStation) || this.downStation.equals(section.upStation);
    }

    public boolean hasAnyMatchedThisDownStation(Section section) {
        return this.upStation.equals(section.downStation) || this.downStation.equals(section.downStation);
    }

    public boolean hasSameUpStation(Section section) {
        return this.upStation.equals(section.upStation);
    }

    public boolean hasSameDownStation(Section section) {
        return this.downStation.equals(section.downStation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(line, section.line) &&
                Objects.equals(upStation, section.upStation) &&
                Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, upStation, downStation);
    }
}
