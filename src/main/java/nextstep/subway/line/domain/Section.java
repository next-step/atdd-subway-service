package nextstep.subway.line.domain;

import static nextstep.subway.exception.domain.SubwayExceptionMessage.OVER_THE_DISTANCE;

import com.google.common.collect.Sets;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.exception.domain.SubwayException;
import nextstep.subway.generic.domain.Distance;
import nextstep.subway.station.domain.Station;

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

    @Column(name = "distance")
    private Distance distance;

    public Section() {
    }

    public Section(final Line line, final Station upStation, final Station downStation, final Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this(line, upStation, downStation, Distance.valueOf(distance));
    }

    public static Section mergeSection(final Section sectionByDownStation, final Section sectionByUpStation) {
        return new Section(sectionByDownStation.line, sectionByDownStation.upStation, sectionByUpStation.downStation, sectionByUpStation.plusDistance(sectionByDownStation));
    }

    private Distance plusDistance(final Section section) {
        return distance.plus(section.distance);
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

    public boolean hasUpStation(final Station upStation) {
        return this.upStation.equals(upStation);
    }

    public boolean hasDownStation(final Station downStation) {
        return this.downStation.equals(downStation);
    }

    public boolean intersects(final Section section) {
        return hasUpStation(section) || hasDownStation(section);
    }

    public boolean equalsStations(final Section section) {
        return hasUpStation(section) && hasDownStation(section);
    }

    public boolean hasUpStation(final Section section) {
        return this.upStation.equals(section.upStation);
    }

    public boolean hasDownStation(final Section section) {
        return this.downStation.equals(section.downStation);
    }

    public void rearrange(final Section section) {
        validateDistance(section);

        if (section.hasUpStation(this)) {
            this.upStation = section.downStation;
            this.distance = distance.minus(section.distance);
            return;
        }

        this.downStation = section.upStation;
        this.distance = distance.minus(section.distance);
    }

    public boolean hasStation(final Station station) {
        return Sets.newHashSet(upStation, downStation)
                .contains(station);
    }

    private void validateDistance(final Section section) {
        if (section.isGreaterThanOrEqualsToDistance(this)) {
            throw new SubwayException(OVER_THE_DISTANCE);
        }
    }

    private boolean isGreaterThanOrEqualsToDistance(final Section section) {
        return distance.isGreaterThanOrEqualsTo(section.distance);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Section)) {
            return false;
        }
        final Section section = (Section) o;
        return Objects.equals(id, section.id) && Objects.equals(line, section.line)
                && Objects.equals(upStation, section.upStation) && Objects.equals(downStation,
                section.downStation) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line, upStation, downStation, distance);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", line=" + line +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
    }
}
