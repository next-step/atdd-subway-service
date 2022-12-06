package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidDistanceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    private static final int SURCHARGE_DISTANCE_STEP1 = 10;
    private static final int SURCHARGE_DISTANCE_STEP2 = 50;

    @Column(name = "distance")
    private int value;

    protected Distance() {
        this.value = 0;
    }

    public Distance(int value) {
        if (isValid(value)) {
            throw new InvalidDistanceException("거리는 0보다 작을 수 없습니다.");
        }
        this.value = value;
    }

    public static Distance sum(Distance distance1, Distance distance2) {
        return new Distance(distance1.value + distance2.value);
    }

    public Distance subtract(Distance other) {
        if (isValid(this.value - other.value)) {
            throw new InvalidDistanceException("거리의 차이는 0보다 작을 수 없습니다.");
        }
        return new Distance(this.value - other.value);
    }

    public boolean isDefaultDistance() {
        return value <= SURCHARGE_DISTANCE_STEP1;
    }

    public boolean isMiddleDistance() {
        return value > SURCHARGE_DISTANCE_STEP1 && value <= SURCHARGE_DISTANCE_STEP2;
    }

    public boolean isLongDistance() {
        return value > SURCHARGE_DISTANCE_STEP2;
    }

    public int getDistance() {
        return value;
    }

    private static boolean isValid(int value) {
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
