package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidDistanceException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;
import java.util.stream.Stream;

@Entity
public class Section {

    private static final String NEW_SECTION_DISTANCE_MUST_SHORTER_THAN_EXIST_ONE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";
    private static final String ONLY_CONNECTED_SECTION_CAN_REMOVE_SHARED_STATION = "연속 된 구간에서 겹치는 역만 제거할 수 있습니다.";

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

    @Column(nullable = false)
    @Embedded
    private Distance distance;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
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

    public void updateUpStation(Section newSection) {
        validateNewSectionDistance(newSection);
        this.upStation = newSection.downStation;
        this.distance = distance.subtract(newSection.distance);
    }

    public void updateDownStation(Section newSection) {
        validateNewSectionDistance(newSection);
        this.downStation = newSection.upStation;
        this.distance = distance.subtract(newSection.distance);
    }

    public boolean containsStation(Long stationIdForRemove) {
        return upStation.isSameId(stationIdForRemove) || downStation.isSameId(stationIdForRemove);
    }

    public boolean containsStation(Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    public void insertNewSection(Section newSection) {
        if (this.isSameUpStation(newSection)) {
            this.updateUpStation(newSection);
        }
        if (this.isSameDownStation(newSection)) {
            this.updateDownStation(newSection);
        }
    }

    public Stream<Station> getUpAndDownStationStream() {
        return Stream.of(this.getUpStation(), this.getDownStation());
    }

    public boolean containsAsUpStation(Long stationId) {
        return upStation.isSameId(stationId);
    }

    public boolean containsAsDownStation(Long stationId) {
        return downStation.isSameId(stationId);
    }

    private void validateNewSectionDistance(Section newSection) {
        if (!distance.isFartherThan(newSection.distance)) {
            throw new InvalidDistanceException(NEW_SECTION_DISTANCE_MUST_SHORTER_THAN_EXIST_ONE);
        }
    }

    private boolean isSameDownStation(Section other) {
        Station thisDownStation = this.getDownStation();
        Station otherDownStation = other.getDownStation();
        return thisDownStation.equals(otherDownStation);
    }

    private boolean isSameUpStation(Section other) {
        Station thisUpStation = this.getUpStation();
        Station otherUpStation = other.getUpStation();
        return thisUpStation.equals(otherUpStation);
    }

    public void removeStationBetweenSections(Section other) {
        if(!this.downStation.equals(other.upStation)) {
            throw new IllegalArgumentException(ONLY_CONNECTED_SECTION_CAN_REMOVE_SHARED_STATION);
        }
        this.downStation = other.downStation;
        this.distance = this.distance.add(other.distance);
    }

    public void addLine(Line line) {
        this.line = line;
    }

    public boolean isSameEdges(Section other) {
        return this.getUpStation().equals(other.getUpStation()) &&
            this.getDownStation().equals(other.getDownStation());
    }

    public boolean isNextSection(Section other) {
        return this.getUpStation().equals(other.getDownStation());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Section)) return false;
        Section section = (Section) o;
        return Objects.equals(this.getId(), section.getId()) &&
            Objects.equals(this.getLine(), section.getLine()) &&
            Objects.equals(this.getUpStation(), section.getUpStation()) &&
            Objects.equals(this.getDownStation(), section.getDownStation()) &&
            Objects.equals(this.getDistance(), section.getDistance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLine(), getUpStation(), getDownStation(), getDistance());
    }
}
