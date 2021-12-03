package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section extends BaseEntity implements Comparable<Section>{
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

    public static Section create(Line line, Station upStation, Station downStation, int distance) {
        return new Section(line, upStation, downStation, Distance.valueOf(distance));
    }

    public Section toLine(Line line) {
        if (this.line != null) {
            this.line.removeSection(this);
        }

        this.line = line;
        if (!line.hasSection(this)) {
            line.addSection(this);
        }
        return this;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance.get();
    }

    public boolean isUpStation(Station upStation) {
        return this.upStation.equals(upStation);
    }

    public boolean isDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public List<Station> getUpDownStations() {
        return Arrays.asList(upStation, downStation);
    }

    public void updateUpStation(Station station, int newDistance) {
        update(station, this.downStation, distance.minus(newDistance));
    }

    public void updateDownStation(Station station, int newDistance) {
        update(this.upStation, station, distance.minus(newDistance));
    }

    private void update(Station upStation, Station downStation, Distance distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    @Override
    public int compareTo(Section o) {
        if (this.upStation.equals(o.downStation)) {
            return 1;
        }

        if (this.downStation.equals(o.upStation)) {
            return -1;
        }

        return 0;
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
        return Objects.equals(id, section.id) && Objects.equals(line, section.line)
            && Objects.equals(upStation, section.upStation) && Objects
            .equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line, upStation, downStation);
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
