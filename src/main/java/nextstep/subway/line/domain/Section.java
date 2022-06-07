package nextstep.subway.line.domain;

import java.util.Objects;
import nextstep.subway.BaseEntity;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @Embedded
    private Distance distance;

    protected Section() {
    }

    public Section(SectionBuilder sectionBuilder) {
        this.id = sectionBuilder.id;
        this.line = sectionBuilder.line;
        this.upStation = sectionBuilder.upStation;
        this.downStation = sectionBuilder.downStation;
        this.distance = sectionBuilder.distance;
    }

    public static SectionBuilder builder(Line line, Station upStation, Station downStation, Distance distance) {
        return new SectionBuilder(line, upStation, downStation, distance);
    }

    public static class SectionBuilder {
        private Long id;
        private final Line line;
        private final Station upStation;
        private final Station downStation;
        private final Distance distance;

        private SectionBuilder(Line line, Station upStation, Station downStation, Distance distance) {
            validateParameter(line, upStation, downStation, distance);
            this.line = line;
            this.upStation = upStation;
            this.downStation = downStation;
            this.distance = distance;
        }

        private void validateParameter(Line line, Station upStation, Station downStation, Distance distance) {
            validateLineNotNull(line);
            validateUpStationNotNull(upStation);
            validateDownStationNotNull(downStation);
            validateDistanceNotNull(distance);
        }

        private void validateLineNotNull(Line line) {
            if (Objects.isNull(line)) {
                throw new NotFoundException("노선 정보가 없습니다.");
            }
        }

        private void validateUpStationNotNull(Station upStation) {
            if (Objects.isNull(upStation)) {
                throw new NotFoundException("상행역 정보가 없습니다.");
            }
        }

        private void validateDownStationNotNull(Station downStation) {
            if (Objects.isNull(downStation)) {
                throw new NotFoundException("하행역 정보가 없습니다.");
            }
        }
        private void validateDistanceNotNull(Distance distance) {
            if (Objects.isNull(distance)) {
                throw new NotFoundException("구간 거리 정보가 없습니다.");
            }
        }

        public SectionBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public Section build() {
            return new Section(this);
        }
    }

    public Long id() {
        return id;
    }

    public Line line() {
        return line;
    }

    public Station upStation() {
        return upStation;
    }

    public Station downStation() {
        return downStation;
    }

    public Distance distance() {
        return distance;
    }

    public void update(Section newSection) {
        if (isEqualsUpStation(newSection)) {
            updateUpStation(newSection);
        }
        if (isEqualsDownStation(newSection)) {
            updateDownStation(newSection);
        }
    }

    private boolean isEqualsUpStation(Section newSection) {
        return this.upStation().equals(newSection.upStation());
    }

    private void updateUpStation(Section newSection) {
        validateLongerThan(newSection.distance());
        distance.minus(newSection.distance);
        this.upStation = newSection.downStation();
    }

    private boolean isEqualsDownStation(Section newSection) {
        return this.downStation().equals(newSection.downStation());
    }

    private void updateDownStation(Section newSection) {
        validateLongerThan(newSection.distance());
        distance.minus(newSection.distance);
        this.downStation = newSection.upStation();
    }

    private void validateLongerThan(Distance newDistance) {
        if (!isLongerThan(newDistance)) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    private boolean isLongerThan(Distance newDistance) {
        return this.distance.distance() > newDistance.distance();
    }
}
