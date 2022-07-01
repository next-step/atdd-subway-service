package nextstep.subway.line.domain;

import nextstep.subway.path.domain.DistanceFarePolicy;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {

    @Column
    private int value;

    protected Distance() {
    }

    public Distance(int value) {
        this.value = value;
    }

    public static Distance sum(Distance d1, Distance d2) {
        return new Distance(d1.value + d2.value);
    }

    public static Distance minus(Distance d1, Distance d2) {
        return new Distance(d1.value - d2.value);
    }

    public void minus(Distance distance) {
        if (value <= distance.value) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.value -= distance.value;
    }

    public int getValue() {
        return value;
    }

    public boolean isBigger(Distance distance) {
        return this.value > distance.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
