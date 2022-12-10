package nextstep.subway.line.domain;

import static nextstep.subway.fare.domain.Fare.FREE;

import java.util.Objects;
import javax.persistence.Embeddable;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.exception.IllegalDistanceException;

@Embeddable
public class Distance {

    private static final int LONG_DISTANCE = 50;
    private static final int LONG_DISTANCE_CHARGE_RANGE = 8;
    private static final int MIDDLE_DISTANCE = 10;
    private static final int MIDDLE_DISTANCE_CHARGE_RANGE = 5;

    private int distance;

    public Distance(int distance) {
        this.distance = distance;
    }

    public Distance() {

    }

    public Distance minus(final Distance newDistance) {
        if (this.distance <= newDistance.distance) {
            throw new IllegalDistanceException();
        }
        return new Distance(this.distance - newDistance.distance);
    }

    public Distance plus(Distance newDistance) {
        return new Distance(this.distance + newDistance.distance);
    }

    public Fare additionalFareByDistance() {
        if (this.hasLongDistance()) {
            int additionalDistance = distance - LONG_DISTANCE;
            return new Fare(calculateFare(additionalDistance, LONG_DISTANCE_CHARGE_RANGE));
        }

        if (this.hasMiddleDistance()) {
            int additionalDistance = distance - MIDDLE_DISTANCE;
            return new Fare(calculateFare(additionalDistance, MIDDLE_DISTANCE_CHARGE_RANGE));
        }

        return FREE;
    }

    private int calculateFare(int additionalDistance, int additionalFare) {
        return (int) ((Math.ceil((additionalDistance - 1) / additionalFare) + 1) * 100);
    }

    private boolean hasLongDistance() {
        return this.distance > LONG_DISTANCE;
    }

    private boolean hasMiddleDistance() {
        return this.distance > MIDDLE_DISTANCE;
    }

    public int distance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
