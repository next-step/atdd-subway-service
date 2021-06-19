package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.domain.wrapper.Distance;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Table(name = "section")
@Entity
public class Section extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
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
        this.distance = Distance.from(distance);
    }

    public void updateSectionStationByAddNewSection(final Section newSection) {
        if (isSameUpStation(newSection.getUpStation())) {
            updateUpStation(newSection);
        }
        if (isSameDownStation(newSection.getDownStation())) {
            updateDownStation(newSection);
        }
    }

    public boolean isSameDownStation(final Station otherDownStation) {
        return this.downStation.equals(otherDownStation);
    }

    public boolean isSameUpStation(final Station otherUpStation) {
        return this.upStation.equals(otherUpStation);
    }

    private void updateDownStation(Section newSection) {
        changeDistanceByNewSectionDistance(newSection.distance);
        this.downStation = newSection.upStation;
    }

    private void updateUpStation(Section newSection) {
        changeDistanceByNewSectionDistance(newSection.distance);
        this.upStation = newSection.downStation;
    }

    private void changeDistanceByNewSectionDistance(Distance newSectionDistance) {
        this.distance = distance.distanceDiffWithOtherDistance(newSectionDistance);
    }

    public List<Station> getUpAndDownStation() {
        return Stream.of(upStation, downStation).collect(toList());
    }

    public boolean isAfter(Section other) {
        return this.upStation.equals(other.getDownStation());
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
        return distance.getDistance();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return distance == section.distance && Objects.equals(id, section.id) && Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStation, downStation, distance);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
    }
}
