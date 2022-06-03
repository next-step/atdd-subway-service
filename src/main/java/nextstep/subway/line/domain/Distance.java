package nextstep.subway.line.domain;

import static nextstep.subway.line.enums.LineExceptionType.DISTANCE_IS_MUST_BE_GREATER_THAN_1;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private int distance;

    protected Distance() {}

    private Distance(int distance) {
        this.distance = distance;
    }

    public static Distance from(int distance) {
        validateDistance(distance);
        return new Distance(distance);
    }

    public boolean isGraterThanOrEquals(Distance distance) {
        return this.distance >= distance.distance;
    }

    public void minus(Distance distance) {
        this.distance -= distance.distance;
    }

    public void plus(Distance distance) {
        this.distance += distance.distance;
    }

    public int getValue() {
        return this.distance;
    }

    private static void validateDistance(int distance) {
        if (distance < 1) {
            throw new IllegalArgumentException(DISTANCE_IS_MUST_BE_GREATER_THAN_1.getMessage());
        }
    }
}
