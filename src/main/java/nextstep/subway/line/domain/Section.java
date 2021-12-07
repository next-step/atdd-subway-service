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
import nextstep.subway.common.BaseEntity;
import nextstep.subway.exception.InvalidArgumentException;
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
        setLine(line);
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

    public void setLine(Line line) {
        if (this.line != null) {
            this.line.removeSection(this);
        }

        this.line = line;
        line.add(this);
    }

    public void removeLine() {
        if (this.line != null) {
            this.line = null;
        }
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

    public boolean isIncludeStation(Station station) {
        return isUpStation(station) || isDownStation(station);
    }

    public boolean isDownStationOfSection(Section section) {
        return this.downStation.equals(section.downStation);
    }

    public boolean isUpStationOfSection(Section section) {
        return this.upStation.equals(section.upStation);
    }

    public boolean equalsDistance(int distance) {
        return this.distance.equals(Distance.valueOf(distance));
    }

    public boolean isUpStation(Station upStation) {
        return this.upStation.equals(upStation);
    }

    public boolean isDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public boolean isTheDownLine(Section section) {
        return isUpStation(section.downStation);
    }

    public boolean isTheUpLine(Section section) {
        return isDownStation(section.upStation);
    }

    public Stream<Station> getUpDownStations() {
        return Stream.of(upStation, downStation);
    }

    public void updateUpStationBySection(Section section){
        update(section.downStation, this.downStation, minus(section));
    }

    public void updateDownStationBySection(Section section) {
        update(this.upStation, section.upStation, minus(section));
    }

    public void updateConnect(Section section) {
        if (isTheUpLine(section)) {
            update(this.upStation, section.downStation, this.distance.plus(section.distance));
            return;
        }

        if (isTheDownLine(section)) {
            update(section.upStation, this.downStation, this.distance.plus(section.distance));
            return;
        }
    }

    protected boolean equalsLine(Line line) {
        if (this.line == null) {
            return false;
        }
        return this.line.equals(line);
    }

    protected boolean equalsSection(Section section) {
        return upStation.equals(section.upStation) && downStation.equals(section.downStation);
    }

    private Distance minus(Section section) {
        try {
            return distance.minus(section.distance);
        } catch (InvalidArgumentException e) {
            throw new InvalidArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요.");
        }
    }

    private void update(Station upStation, Station downStation, Distance distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private Long getId() {
        return id;
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
        return Objects.equals(getId(), section.getId());
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
