package nextstep.subway.line.domain;

import static javax.persistence.FetchType.*;

import java.util.Objects;
import java.util.stream.Stream;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private int distance;

    protected Section() { }

    Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    Long getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    boolean connectIfHasEqualStation(Section section) {
        if (hasEqualDownStation(section)) {
            changeDownStationToUpStationOf(section);
            return true;
        }
        if (hasEqualUpStation(section)) {
            changeUpStationToDownStationOf(section);
            return true;
        }
        return false;
    }

    boolean connectIfAdjacentByStation(Section section, Station station) {
        if (isConnectedUpwardByStation(section, station)) {
            changeDownStationToDownStationOf(section);
            return true;
        }
        if (isConnectedDownwardByStation(section, station)) {
            changeUpStationToUpStationOf(section);
            return true;
        }
        return false;
    }

    boolean isUpwardOf(Section other) {
        return this.downStation.equals(other.upStation);
    }

    boolean isDownwardOf(Section other) {
        return this.upStation.equals(other.downStation);
    }

    Stream<Station> getStations() {
        return Stream.of(upStation, downStation);
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    boolean hasEqualUpStation(Section section) {
        return this.upStation.equals(section.upStation);
    }

    boolean hasEqualDownStation(Section section) {
        return this.downStation.equals(section.downStation);
    }

    boolean contain(Station station) {
        return containUpwardStation(station) || containDownwardStation(station);
    }

    private void changeUpStationToDownStationOf(Section section) {
        validateLongerThan(section);
        this.upStation = section.downStation;
        this.distance -= section.distance;
    }

    private void changeDownStationToUpStationOf(Section section) {
        validateLongerThan(section);
        this.downStation = section.upStation;
        this.distance -= section.distance;
    }

    private void validateLongerThan(Section section) {
        if (this.distance <= section.distance) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    private void changeUpStationToUpStationOf(Section adjacentSection) {
        this.upStation = adjacentSection.upStation;
        this.distance += adjacentSection.distance;
    }

    private void changeDownStationToDownStationOf(Section adjacentSection) {
        this.downStation = adjacentSection.downStation;
        this.distance += adjacentSection.distance;
    }

    private boolean isConnectedDownwardByStation(Section adjacentSection, Station station) {
        return containUpwardStation(station) && isDownwardOf(adjacentSection);
    }

    private boolean isConnectedUpwardByStation(Section adjacentSection, Station station) {
        return containDownwardStation(station) && isUpwardOf(adjacentSection);
    }

    private boolean containUpwardStation(Station station) {
        return upStation.equals(station);
    }

    private boolean containDownwardStation(Station station) {
        return this.downStation.equals(station);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Section section = (Section)o;
        return Objects.equals(getId(), section.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

