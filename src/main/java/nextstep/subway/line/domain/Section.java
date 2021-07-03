package nextstep.subway.line.domain;

import nextstep.subway.line.domain.wrappers.Distance;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @Embedded
    private Distance distance = new Distance();

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public Section(Long id, Station upStation, Station downStation, int distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public void lineBy(Line line) {
        this.line = line;
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
        this.upStation = station;
        this.distance = newDistance;
    }

    public void updateDownStation(Station station, Distance newDistance) {
        this.downStation = station;
        this.distance = newDistance;
    }

    public Distance createNewDistanceBySubtract(Section other) {
        return distance.subtractDistance(other.distance);
    }

    public Distance createNewDistanceBySum(Section other) {
        return distance.sumDistance(other.distance);
    }

    public Section calcFirstSection(Section section) {
        if (Objects.isNull(section) || downStation.isSameId(section.upStation) || upStation.isSameId(section.upStation)) {
            return this;
        }
        return section;
    }

    public boolean isNextSection(Section section) {
        return upStation.isSameId(section.downStation);
    }

    public boolean isContainStation(Section section) {
        return upStation.isSameId(section.upStation) || upStation.isSameId(section.downStation)
                || downStation.isSameId(section.upStation) || downStation.isSameId(section.downStation);
    }

    public boolean isSameUpStation(Section section) {
        return upStation.isSameId(section.upStation);
    }

    public boolean isSameUpStation(Station station) {
        return upStation.isSameId(station);
    }

    public boolean isSameDownStation(Section section) {
        return downStation.isSameId(section.downStation);
    }

    public boolean isSameDownStation(Station station) {
        return downStation.isSameId(station);
    }

    public boolean isSameStations(Section section) {
        return upStation.isSameId(section.upStation) && downStation.isSameId(section.downStation);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Section section = (Section) object;
        return Objects.equals(id, section.id) &&
                Objects.equals(line, section.line) &&
                Objects.equals(upStation, section.upStation) &&
                Objects.equals(downStation, section.downStation) &&
                Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line, upStation, downStation, distance);
    }
}
