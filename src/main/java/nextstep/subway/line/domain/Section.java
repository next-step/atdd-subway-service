package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

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

    @Embedded
    private Distance distance;

    protected Section() {
    }

    private Section(Line line, Station upStation, Station downStation, int distance) {
        validateDuplicate(upStation, downStation);
        this.line = Objects.requireNonNull(line, "노선의 정보가 입력되지 않았습니다.");
        this.upStation = Objects.requireNonNull(upStation, "종점역 정보가 입력되지 않았습니다.");
        this.downStation = Objects.requireNonNull(downStation, "종점역 정보가 입력되지 않았습니다.");
        this.distance = Distance.from(distance);
    }

    public static Section of(Line line, Station upStation, Station downStation, int distance) {
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

    public void changeUpStationToAddSectionDownStation(Section addSection) {
        validateDistance(addSection);
        this.upStation = addSection.getDownStation();
        this.distance = this.distance.minus(addSection.distance);
    }

    public void changeDownStationToRemoveSectionUpStation(Section addSection) {
        validateDistance(addSection);
        this.downStation = addSection.getUpStation();
        this.distance = this.distance.minus(addSection.distance);
    }

    public void merge(Section removeSection) {
        this.downStation = removeSection.getDownStation();
        this.distance = this.distance.plus(removeSection.distance);
    }

    public boolean hasSameDownStation(Station otherStation) {
        return this.downStation == otherStation;
    }

    public boolean hasSameUpStation(Station otherStation) {
        return this.upStation == otherStation;
    }

    private void validateDuplicate(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new RuntimeException("상행선과 하행선은 같을 수 없습니다.");
        }
    }

    private void validateDistance(Section addSection) {
        if (this.distance.isLessThanEqualTo(addSection.distance)) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(getId(), section.getId())
                && Objects.equals(getLine(), section.getLine())
                && Objects.equals(getUpStation(), section.getUpStation())
                && Objects.equals(getDownStation(), section.getDownStation())
                && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLine(), getUpStation(), getDownStation(), distance);
    }
}
