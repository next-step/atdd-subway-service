package nextstep.subway.line.domain;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

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

    @Embedded
    private Distance distance;

    protected Section() {
    }

    private Section(Long id, Line line, Station upStation, Station downStation, Distance distance) {
        this.id = id;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Line line, Station upStation, Station downStation, int distance) {
        return new Section(null, line, upStation, downStation, Distance.from(distance));
    }

    public static Section of(Long id, Station upStation, Station downStation) {
        return new Section(id, null, upStation, downStation, Distance.zero());
    }

    public static Section of(Long id, Station upStation, Station downStation, int distance) {
        return new Section(id, null, upStation, downStation, Distance.from(distance));
    }

    public void modifyUpStation(Section newSection) {
        this.upStation = newSection.downStation;
        this.distance = this.distance.sub(newSection.distance);
    }

    public void modifyDownStation(Section newSection) {
        this.upStation = newSection.upStation;
        this.distance = this.distance.sub(newSection.distance);
    }

    public boolean hasUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public boolean hasDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public boolean isUpStationExisted(Stations stations) {
        return stations.contains(this.upStation);
    }

    public boolean isDownStationExisted(Stations stations) {
        return stations.contains(this.downStation);
    }

    public boolean isUpStationNotExisted(Stations stations) {
        return !isUpStationExisted(stations);
    }

    public boolean isDownStationNotExisted(Stations stations) {
        return !isDownStationExisted(stations);
    }

    public boolean hasSameUpStation(Section other) {
        return this.upStation.equals(other.upStation);
    }

    public boolean hasSameDownStation(Section other) {
        return this.downStation.equals(other.downStation);
    }

    public Section merge(Section other) {
        return new Section(null, this.line, this.upStation, other.downStation, this.distance.sum(other.distance));
    }

    public boolean hasDistance(Distance distance) {
        return this.distance.equals(distance);
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistanceValue() {
        return this.distance.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Section)) {
            return false;
        }
        Section section = (Section)o;
        return Objects.equals(getId(), section.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
