package nextstep.subway.line.domain;

import java.util.Objects;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

@Entity
public class Section extends BaseEntity {
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

    protected Section() {
    }

    private Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        toLine(line);
    }

    private Section(Station upStation, Station downStation, Distance distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section create(Line line, Station upStation, Station downStation, Distance distance) {
        return new Section(line, upStation, downStation, distance);
    }

    public static Section create(Station upStation, Station downStation, Distance distance) {
        return new Section(upStation, downStation, distance);
    }

    public static Section from(Line line, Section upLineStation, Section downLineStation, Distance distance) {
        return new Section(line, upLineStation.upStation, downLineStation.downStation, distance);
    }

    public Section toLine(Line line) {

        if (this.line != null) {
            this.line.removeSection(this);
        }

        this.line = line;
        line.add(this);

        return this;
    }

    public void removeLine() {
        if (this.line != null) {
            this.line = null;
        }
    }

    public Distance plusDistance(Section other) {
        return this.distance.plus(other.distance);
    }

    public boolean isIncludeStation(Station station) {
        return isUpStation(station) || isDownStation(station);
    }

    public boolean isDownStationOfSection(Section section) {
        return this.downStation.equalsName(section.downStation);
    }

    public boolean isUpStationOfSection(Section section) {
        return this.upStation.equalsName(section.upStation);
    }

    public boolean isUpStation(Station upStation) {
        return this.upStation.equalsName(upStation);
    }

    public boolean equalsDistance(int distance) {
        return this.distance.equals(Distance.valueOf(distance));
    }

    public boolean isDownStation(Station station) {
        return this.downStation.equalsName(station);
    }

    public boolean isTheDownLine(Section section) {
        return isUpStation(section.downStation);
    }

    public boolean isTheUpLine(Section section) {
        return isDownStation(section.upStation);
    }

    public boolean equalsStations(Section section) {
        return upStation.equalsName(section.upStation)
            && downStation.equalsName(section.downStation);
    }

    public boolean equalsLine(Line line) {
        if (this.line == null) {
            return false;
        }
        return this.line.equals(line);
    }

    public Stream<Station> getUpDownStations() {
        return Stream.of(upStation, downStation);
    }

    public void updateUpStationBySection(Section section){
        update(section.downStation, this.downStation, distance.minus(section.distance));
    }

    public void updateDownStationBySection(Section section) {
        update(this.upStation, section.upStation, distance.minus(section.distance));
    }

    private void update(Station upStation, Station downStation, Distance distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
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
        return Objects.equals(id, section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
