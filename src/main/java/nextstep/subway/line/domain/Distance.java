package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    private static final int BASIC_FARE = 1250;
    private static final int MIN_DISTANCE = 0;
    private static final int FIRST_OVER_FARE_PER_VALUE = 5;
    private static final int SECOND_OVER_FARE_PER_VALUE = 8;
    private static final int FIRST_OVER_FARE_MIN_DISTANCE = 10;
    private static final int SECOND_OVER_FARE_MIN_DISTANCE = 50;
    private static final int PER_FARE = 100;

    @Column
    private final int distance;

    public Distance(int distance) {
        if (distance <= MIN_DISTANCE) {
            throw new IllegalArgumentException("거리는 0보다 작거나 같을 수 없습니다.");
        }
        this.distance = distance;
    }

    public Distance() {
        distance = MIN_DISTANCE;
    }

    public Distance plus(Distance distance) {
        return new Distance(this.distance + distance.getDistance());
    }

    public Distance minus(Distance distance) {
        return new Distance(this.distance - distance.getDistance());
    }

    public int getDistance() {
        return distance;
    }

    public int getDistanceFare() {
        return BASIC_FARE + getOverFare();
    }

    private int getOverFare() {
        if (distance <= FIRST_OVER_FARE_MIN_DISTANCE) {
            return 0;
        }
        if (distance <= SECOND_OVER_FARE_MIN_DISTANCE) {
            return calculateOverFare(distance - FIRST_OVER_FARE_MIN_DISTANCE, FIRST_OVER_FARE_PER_VALUE);
        }
        int firstValue = calculateOverFare(SECOND_OVER_FARE_MIN_DISTANCE - FIRST_OVER_FARE_MIN_DISTANCE, FIRST_OVER_FARE_PER_VALUE);
        int secondValue = calculateOverFare(distance - SECOND_OVER_FARE_MIN_DISTANCE, SECOND_OVER_FARE_PER_VALUE);
        return firstValue + secondValue;
    }

    private int calculateOverFare(int distance, int perValue) {
        return (int) ((Math.ceil((distance - 1) / perValue) + 1) * PER_FARE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
