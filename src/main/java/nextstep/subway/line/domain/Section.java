package nextstep.subway.line.domain;

import nextstep.subway.line.message.SectionMessage;
import nextstep.subway.path.application.LinePathGraph;
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

    public Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section merge(Section upLineStationSection, Section downLineStationSection) {
        if(!upLineStationSection.line.equals(downLineStationSection.line)) {
            throw new IllegalArgumentException(SectionMessage.MERGE_ERROR_NOT_EQUALS_LINE.message());
        }

        return new Section(
                upLineStationSection.line,
                upLineStationSection.upStation,
                downLineStationSection.downStation,
                upLineStationSection.distance.plus(downLineStationSection.distance)
        );
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
        this.distance = distance.minus(newDistance);
    }

    public void updateDownStation(Station station, Distance newDistance) {
        this.downStation = station;
        this.distance = distance.minus(newDistance);
    }

    public boolean isSameUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public boolean isSameDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public void addGraphEdge(LinePathGraph graph) {
        graph.addEdge(getUpStation(), getDownStation(), getDistance());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        if (!Objects.equals(id, section.id)) return false;
        if (!Objects.equals(line, section.line)) return false;
        if (!Objects.equals(upStation, section.upStation)) return false;
        return Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (line != null ? line.hashCode() : 0);
        result = 31 * result + (upStation != null ? upStation.hashCode() : 0);
        result = 31 * result + (downStation != null ? downStation.hashCode() : 0);
        return result;
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
