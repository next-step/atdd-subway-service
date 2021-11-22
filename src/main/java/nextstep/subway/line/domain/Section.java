package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.station.domain.Station;
import org.springframework.util.Assert;

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

    private Distance distance;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, Distance distance) {
        validate(upStation, downStation, distance);
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Station upStation, Station downStation, Distance distance) {
        return new Section(null, upStation, downStation, distance);
    }

    public Long id() {
        return id;
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

    void remove(Section section) {
        validateRemoval(section);
        cutSection(section);
        minusDistance(section.distance);
    }

    public List<Station> stations() {
        return Arrays.asList(upStation, downStation);
    }

    private void validateRemoval(Section section) {
        Assert.notNull(section, "지워지는 구간은 null 일 수 없습니다.");
        validateRemovedSection(section);
        validateSubtractDistance(section.distance);
    }

    private void cutSection(Section section) {
        if (this.upStation.equals(section.upStation)) {
            upStation = section.downStation;
            return;
        }
        downStation = section.upStation;
    }

    private void validateRemovedSection(Section section) {
        if (equalOrNotOverlapping(section)) {
            throw new InvalidDataException(
                String.format("현재 구간(%s)에서 해당 구간(%s)을 제거할 수 없습니다.", this, section));
        }
    }

    private boolean equalOrNotOverlapping(Section section) {
        return this.upStation.equals(section.upStation) ==
            this.downStation.equals(section.downStation);
    }

    private void minusDistance(Distance distance) {
        this.distance = this.distance.subtract(distance);
    }

    private void validateSubtractDistance(Distance distance) {
        if (this.distance.lessThanOrEqual(distance)) {
            throw new InvalidDataException(
                String.format("역과 역 사이의 거리(%s)보다 좁은 거리(%s)를 입력해주세요", this.distance, distance));
        }
    }

    private void validate(Station upStation, Station downStation, Distance distance) {
        Assert.notNull(upStation, "'upStation' 값은 null 일 수 없습니다.");
        Assert.notNull(downStation, "'downStation' 값은 null 일 수 없습니다.");
        Assert.notNull(distance, "'distance' 값은 null 일 수 없습니다.");
        Assert.isTrue(!upStation.equals(downStation),
            String.format("상행역(%s)과 하행역은(%s) 같을 수 없습니다.",
                upStation, downStation));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStation, downStation, distance);
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
        return Objects.equals(id, section.id) && Objects
            .equals(upStation, section.upStation) && Objects
            .equals(downStation, section.downStation) && Objects
            .equals(distance, section.distance);
    }

    @Override
    public String toString() {
        return "Section{" +
            "id=" + id +
            ", upStation=" + upStation +
            ", downStation=" + downStation +
            ", distance=" + distance +
            '}';
    }
}
