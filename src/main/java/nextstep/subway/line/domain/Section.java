package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import nextstep.subway.station.domain.Station;
import javax.persistence.*;

@Entity
public class Section {

    private static final String INVALID_LINE_MESSAGE = "노선 정보가 없습니다.";

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

    public Section() {
    }

    private Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Line line, Station upStation, Station downStation, Distance distance) {
        return new Section(line, upStation, downStation, distance);
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

    public void update(Section target) {
        if (this.upStation.equals(target.upStation)) {
            this.upStation = target.downStation;
            this.distance = subtractDistance(target);
        }
        if (this.downStation.equals(target.downStation)) {
            this.downStation = target.upStation;
            this.distance = subtractDistance(target);
        }
    }

    private Distance subtractDistance(Section target) {
        return this.distance.subtract(target.distance);
    }

    public boolean isSameStationPair(Section target) {
        return this.upStation.equals(target.upStation)
            && this.downStation.equals(target.downStation);
    }

    public List<Station> getStationPair() {
        return Arrays.asList(this.upStation, this.downStation);
    }

    public void setLine(Line line) {
        validateLineNotNull(line);
        this.line = line;
    }

    private void validateLineNotNull(Line line) {
        if (Objects.isNull(line)) {
            throw new IllegalArgumentException(INVALID_LINE_MESSAGE);
        }
    }

    public boolean equalsUpStation(Station target) {
        return this.upStation.equals(target);
    }

    public boolean equalsDownStation(Station target) {
        return this.downStation.equals(target);
    }

    public static Distance newDistance(Section upSection, Section downSection) {
        return upSection.distance
            .add(downSection.distance);
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
        return Objects.equals(id, section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
