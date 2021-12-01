package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section {
    private static final String CREATION_FAIL_ERROR_MESSAGE = "Section 생성에 필요한 필수 정보를 확인해주세요. upStation=%s, downStation=%s, distance=%s";
    private static final String SAME_UP_AND_DOWN_STATION_ERROR_MESSAGE = "상행역과 하행역은 같을 수 없습니다. upStation=%s, downStation=%s";
    private static final String TOO_LONG_DISTANCE_THAN_SECTION_ERROR_MESSAGE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

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

    protected Section() {}

    private Section(Station upStation, Station downStation, Distance distance) {
        this(null, upStation, downStation, distance);
    }
    private Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Line line, Station upStation, Station downStation, Distance distance) {
        validateCreateSection(upStation, downStation, distance);
        return new Section(line, upStation, downStation, distance);
    }

    public static Section of(Station upStation, Station downStation, Distance distance) {
        validateCreateSection(upStation, downStation, distance);
        return new Section(upStation, downStation, distance);
    }

    public static Section of(Station upStation, Station downStation) {
        return new Section(upStation, downStation, Distance.createEmpty());
    }

    public void updateUpStation(Station station, Distance newDistance) {
        if (this.distance.isLessThanOrEqualTo(newDistance)) {
            throw new IllegalArgumentException(TOO_LONG_DISTANCE_THAN_SECTION_ERROR_MESSAGE);
        }
        this.upStation = station;
        this.distance = this.distance.minus(newDistance);
    }

    public void updateDownStation(Station station, Distance newDistance) {
        if (this.distance.isLessThanOrEqualTo(newDistance)) {
            throw new IllegalArgumentException(TOO_LONG_DISTANCE_THAN_SECTION_ERROR_MESSAGE);
        }
        this.downStation = station;
        this.distance = this.distance.minus(newDistance);
    }

    public boolean isSameDistance(Distance distance) {
        return this.distance.equals(distance);
    }

    public boolean isSameUpAndDownStations(Section section) {
        return isSameUpStation(section.getUpStation()) && isSameDownStation(section.getDownStation());
    }

    public boolean isSameUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public boolean isSameDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public boolean isGreaterThanOrEqualDistanceTo(Section section) {
        return distance.isGreaterThanOrEqualTo(section.distance);
    }

    public List<Station> getStations() {
        return Arrays.asList(upStation, downStation);
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

    private static void validateCreateSection(Station upStation, Station downStation, Distance distance) {
        validateHasRequired(upStation, downStation, distance);
        validateSameUpAndSownStation(upStation, downStation);
    }

    private static void validateHasRequired(Station upStation, Station downStation, Distance distance) {
        if (Objects.isNull(upStation) || Objects.isNull(downStation) || Objects.isNull(distance)) {
            throw new IllegalArgumentException(String.format(CREATION_FAIL_ERROR_MESSAGE, upStation, downStation,
                                                             distance));
        }
    }

    private static void validateSameUpAndSownStation(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException(String.format(SAME_UP_AND_DOWN_STATION_ERROR_MESSAGE, upStation.getId(), downStation.getId()));
        }
    }
}
