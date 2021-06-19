package nextstep.subway.line.domain;

import java.util.Objects;
import java.util.stream.Stream;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

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

    private int distance;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
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

    public int getDistance() {
        return distance;
    }

    boolean connectSection(Section section) {
        if (equals(section)) {
            return false;
        }
        if (hasEqualDownStation(section)) {
            connectDownward(section);
            return true;
        }
        if (hasEqualUpStation(section)) {
            connectUpward(section);
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

    boolean hasEqualUpStation(Section section) {
        return this.upStation.equals(section.upStation);
    }

    boolean hasEqualDownStation(Section section) {
        return this.downStation.equals(section.downStation);
    }

    boolean contain(Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    private void connectUpward(Section section) {
        if (this.distance <= section.distance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = section.downStation;
        this.distance = this.distance - section.distance;
    }

    private void connectDownward(Section section) {
        if (this.distance <= section.distance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.downStation = section.upStation;
        this.distance = this.distance - section.distance;
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


