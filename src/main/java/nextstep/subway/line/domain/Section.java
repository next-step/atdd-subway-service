package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.common.domain.Fare;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.station.domain.Station;
import org.springframework.util.Assert;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "up_station_id", nullable = false)
    private Station upStation;

    @ManyToOne
    @JoinColumn(name = "down_station_id", nullable = false)
    private Station downStation;

    private Distance distance;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    protected Section() {
    }

    private Section(Line line, Station upStation, Station downStation, Distance distance) {
        validate(upStation, downStation, distance);
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Station upStation, Station downStation, Distance distance) {
        return new Section(null, upStation, downStation, distance);
    }

    Long id() {
        return id;
    }

    void changeLine(Line line) {
        this.line = line;
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

    public int distanceValue() {
        return distance.value();
    }

    void cut(Section section) {
        validateCut(section);
        changeStation(section);
        minusDistance(section.distance);
    }

    List<Station> stations() {
        return Arrays.asList(upStation, downStation);
    }

    Section merge(Section section) {
        validateMerge(section);
        if (downStation.equals(section.upStation)) {
            return new Section(line, upStation, section.downStation,
                distance.sum(section.distance));
        }
        return new Section(line, section.upStation, downStation, distance.sum(section.distance));
    }

    Fare extraFare() {
        if (line == null) {
            throw new InvalidDataException(String.format("구간(%s)의 추가 요금을 구할 수 없습니다.", this));
        }
        return line.extraFare();
    }

    private void validateMerge(Section section) {
        Assert.notNull(section, "합쳐지는 구간은 필수입니다.");
        if (doesNotHaveOverlappingStation(section)) {
            throw new InvalidDataException(String.format(
                "합쳐지는 구간들은(%s, %s) 하나의 겹치는 역이 존재해야 합니다.",
                this, section));
        }
    }

    private boolean doesNotHaveOverlappingStation(Section section) {
        return !downStation.equals(section.upStation) && !upStation.equals(section.downStation);
    }

    private void validateCut(Section section) {
        Assert.notNull(section, "지우려는 구간은 필수입니다.");
        validateChangeStation(section);
        validateSubtractDistance(section.distance);
    }

    private void changeStation(Section section) {
        if (this.upStation.equals(section.upStation)) {
            upStation = section.downStation;
            return;
        }
        downStation = section.upStation;
    }

    private void validateChangeStation(Section section) {
        if (equalOrDoesNotHaveOverlappingStation(section)) {
            throw new InvalidDataException(
                String.format("현재 구간(%s)에서 해당 구간(%s)을 제거할 수 없습니다.", this, section));
        }
    }

    private boolean equalOrDoesNotHaveOverlappingStation(Section section) {
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
        Assert.notNull(upStation, "'upStation' 값은 필수입니다.");
        Assert.notNull(downStation, "'downStation' 값은 필수입니다.");
        Assert.notNull(distance, "'distance' 값은 필수입니다.");
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
