package nextstep.subway.line.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

@Entity
public class Section extends BaseEntity {

    public static final String UP_AND_DOWN_STATIONS_CANNOT_BE_THE_SAME = "구간의 상행역과 하행역은 같을 수 없습니다.";
    public static final String DISTANCE_MUST_BE_AT_LEAST_MIN_DISTANCE = "거리는 %d 이상이어야 합니다.";
    public static final String CANNOT_ADD_SECTION_GREATER_THAN_OR_EQUAL_DISTANCE = "기존 역 사이 길이보다 크거나 같은 구간은 추가할 수 없습니다.";
    public static final int MIN_DISTANCE = 0;

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
        validationStations(upStation, downStation);
        validationDistance(distance);
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validationDistance(int distance) {
        if (distance <= MIN_DISTANCE) {
            throw new IllegalArgumentException(String.format(DISTANCE_MUST_BE_AT_LEAST_MIN_DISTANCE, MIN_DISTANCE));
        }
    }

    private void validationStations(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException(UP_AND_DOWN_STATIONS_CANNOT_BE_THE_SAME);
        }
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
        this.upStation = station;
        minusDistance(newDistance);
    }

    protected void minusDistance(int distance) {
        if (isShortEqualThan(distance)) {
            throw new IllegalArgumentException(CANNOT_ADD_SECTION_GREATER_THAN_OR_EQUAL_DISTANCE);
        }
        this.distance -= distance;
    }

    public void updateDownStation(Station station, int newDistance) {
        this.downStation = station;
        plusDistance(newDistance);
    }

    protected void plusDistance(int distance) {
        this.distance += distance;
    }

    protected boolean isBefore(Section section) {
        return downStation.equals(section.getUpStation());
    }

    protected boolean isAfter(Section section) {
        return upStation.equals(section.getDownStation());
    }

    protected boolean isEqualUpStation(Section section) {
        return upStation.equals(section.getUpStation());
    }

    protected boolean isEqualDownStation(Section section) {
        return downStation.equals(section.getDownStation());
    }

    protected boolean isEqualAllStation(Section section) {
        if (!upStation.equals(section.getUpStation())) {
            return false;
        }
        return downStation.equals(section.getDownStation());
    }

    protected boolean isPresentAnyStation(Section section) {
        if (contain(section.getUpStation())) {
            return true;
        }
        return contain(section.getDownStation());
    }

    protected boolean contain(Station station) {
        if (upStation.equals(station)) {
            return true;
        }
        return downStation.equals(station);
    }

    private boolean isShortEqualThan(int distance) {
        return this.distance <= distance;
    }

    public void toLine(Line line) {
        this.line = line;
    }
}
