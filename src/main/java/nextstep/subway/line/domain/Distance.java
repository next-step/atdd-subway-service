package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidDistanceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    @Column(name = "distance")
    private int value;

    protected Distance() {
        this.value = 0;
    }

    public Distance(int value) {
        if (isValidate(value)) {
            throw new InvalidDistanceException("거리는 0보다 작을 수 없습니다.");
        }
        this.value = value;
    }

    public static Distance sum(Distance distance1, Distance distance2) {
        return new Distance(distance1.value + distance2.value);
    }

    public static Distance subtract(Distance distance1, Distance distance2) {
        if (isValidate(distance1.value - distance2.value)) {
            throw new InvalidDistanceException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요.");
        }
        return new Distance(distance1.value - distance2.value);
    }

    public int getDistance() {
        return value;
    }

    private static boolean isValidate(int value) {
        return value < 0;
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
