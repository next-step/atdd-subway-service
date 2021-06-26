package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Section {
    public static final String OUT_BOUND_DISTANCE_ERROR_MESSAGE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
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

    public void updateUpStation(Station station, int newDistance) {
        checkValidOutBoundDistance(newDistance);
        this.upStation = station;
        this.distance -= newDistance;
    }

    public Section calcFirstSection(Section section) {
        if (Objects.isNull(section) || downStation.isSame(section.upStation)) {
            return this;
        }
        return section;
    }

    public boolean isNextSection(Section section) {
        return upStation.isSame(section.downStation);
    }

    public boolean isContainStation(Section section) {
        return upStation.isSame(section.upStation) || upStation.isSame(section.downStation)
                || downStation.isSame(section.upStation) || downStation.isSame(section.downStation);
    }

    public boolean isSameUpStation(Section section) {
        return upStation.isSame(section.upStation);
    }

    public boolean isSameDownStation(Section section) {
        return downStation.isSame(section.downStation);
    }

    public boolean isSameStations(Section section) {
        return upStation.isSame(section.upStation) && downStation.isSame(section.downStation);
    }

    public void updateDownStation(Station station, int newDistance) {
        checkValidOutBoundDistance(newDistance);
        this.downStation = station;
        this.distance -= newDistance;
    }

    private void checkValidOutBoundDistance(int newDistance) {
        if (this.distance <= newDistance) {
            throw new RuntimeException(OUT_BOUND_DISTANCE_ERROR_MESSAGE);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Section section = (Section) object;
        return distance == section.distance &&
                Objects.equals(id, section.id) &&
                Objects.equals(line, section.line) &&
                Objects.equals(upStation, section.upStation) &&
                Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line, upStation, downStation, distance);
    }
}
