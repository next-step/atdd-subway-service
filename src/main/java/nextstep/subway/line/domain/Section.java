package nextstep.subway.line.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.line.domain.wrap.Distance;
import nextstep.subway.station.domain.Station;

@Entity
public class Section {
    public static final String NULL_STATIONS_ERROR_MESSAGE = "구간 생성 시, 상행역과 하행역 정보는 필수 입니다.";
    public static final String EQUALS_STATIONS_ERROR_MESSAGE = "구간 생성 시, 상행역과 하행역 정보는 필수 입니다.";

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

    private Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public static Section of(Line line, Station upStation, Station downStation, int distance) {
        validateStation(upStation, downStation);
        return new Section(line, upStation, downStation, distance);
    }

    private static void validateStation(Station upStation, Station downStation) {
        validateIsNotNullStation(upStation, downStation);
        validateEqualsStations(upStation, downStation);
    }

    private static void validateIsNotNullStation(Station upStation, Station downStation) {
        if (upStation == null || downStation == null) {
            throw new IllegalArgumentException(NULL_STATIONS_ERROR_MESSAGE);
        }
    }

    private static void validateEqualsStations(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException(EQUALS_STATIONS_ERROR_MESSAGE);
        }
    }

    public void update(Section section) {
        if (upStation == section.upStation) {
            updateUpStation(section.downStation, section.getDistance());
        }
        if (downStation == section.downStation) {
            updateDownStation(section.upStation, section.getDistance());
        }
    }

    public void updateUpStation(Station station, int newDistance) {
        this.upStation = station;
        distance.minusDistance(newDistance);
    }

    public void updateDownStation(Station station, int newDistance) {
        this.downStation = station;
        distance.minusDistance(newDistance);
    }

    public Section swapDownStationToTargetDownStation(Section targetSection) {
        distance.plusDistance(targetSection.getDistance());
        return Section.of(this.line, this.upStation, targetSection.downStation, distance.getDistance());
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
}
