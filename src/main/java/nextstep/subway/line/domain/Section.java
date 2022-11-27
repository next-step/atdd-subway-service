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

    protected Section() {
    }

    public Section(final Line line, final Station upStation, final Station downStation, final int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public boolean equalsUpStation(final Station upStation) {
        return this.upStation.equals(upStation);
    }

    public boolean equalsDownStation(final Station downStation) {
        return this.downStation.equals(downStation);
    }

    public void updateUpStation(final Station station, final int newDistance) {
        distance.minusDistance(newDistance);
        this.upStation = station;
    }

    public void updateDownStation(final Station station, final int newDistance) {
        distance.minusDistance(newDistance);
        this.downStation = station;
    }

    public Section updateMiddleStation(final Section downSection) {
        final Station newUpStation = downSection.upStation;
        final Station newDownStation = downStation;
        distance.plusDistance(downSection.getDistance());

        return new Section(line, newUpStation, newDownStation, distance.getValue());
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
        return distance.getValue();
    }

    public boolean matchAllStations(Section section) {
        return this.upStation.equals(section.upStation) &&
                this.downStation.equals(section.downStation);
    }

    public boolean hasUpStations(Section section) {
        return this.upStation.equals(section.upStation) ||
                this.upStation.equals(section.downStation);
    }

    public boolean hasDownStations(Section section) {
        return this.downStation.equals(section.upStation) ||
                this.downStation.equals(section.downStation);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", line=" + line.getId() +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
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
