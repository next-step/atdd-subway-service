package nextstep.subway.line.domain;

import java.util.Objects;

import nextstep.subway.line.exception.InvalidSectionException;
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

    public Section(Station upStation, Station downStation, int distance) {
        this(null, null, upStation, downStation, distance);
    }

    Section(Long id, Station upStation, Station downStation, int distance) {
        this(id, null, upStation, downStation, distance);
    }

    private Section(Line line, Station upStation, Station downStation, int distance) {
        this(null, line, upStation, downStation, distance);
    }

    private Section(Long id, Line line, Station upStation, Station downStation, int distance) {
        this.id = id;
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

    public boolean contains(Station station) {
        return upStation == station
                || downStation == station;
    }

    public boolean isLongerThan(Section section) {
        return this.distance > section.distance;
    }

    public boolean matchesOnlyOneEndOf(Section section) {
        return upStation == section.upStation
                ^ downStation == section.downStation;
    }

    public boolean isMergeableWith(Section section) {
        return upStation == section.downStation
                ^ downStation == section.upStation;
    }

    public void setLine(Line line) {
        checkLine(line);
        this.line = line;
    }

    private void checkLine(Line line) {
        if (Objects.isNull(line)) {
            throw new IllegalArgumentException("소속 노선은 null 이 될 수 없습니다.");
        }

        if (!Objects.isNull(this.line)) {
            throw new InvalidSectionException("소속 노선을 재 정의 할 수는 없습니다.");
        }
    }

    public void updateSection(Section section) {
        checkSection(section);

        if (this.upStation == section.getUpStation()) {
            this.upStation = section.getDownStation();
        }

        if (this.downStation == section.getDownStation()) {
            this.downStation = section.getUpStation();
        }

        this.distance -= section.getDistance();
    }

    private void checkSection(Section section) {
        if (!matchesOnlyOneEndOf(section)) {
            throw new InvalidSectionException("하나의 종단점만 일치해야 합니다.");
        }

        if (!isLongerThan(section)) {
            throw new InvalidSectionException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요.");
        }
    }

    public Section mergeWith(Section section) {
        checkMergeable(section);

        if (this.upStation == section.downStation) {
            return new Section(this.line, section.upStation, this.downStation,
                    this.distance + section.distance);
        }

        return new Section(this.line, this.upStation, section.downStation,
                this.distance + section.distance);
    }

    private void checkMergeable(Section section) {
        if (!isMergeableWith(section)) {
            throw new InvalidSectionException("병합 조건을 만족하지 않습니다.");
        }
    }
}
