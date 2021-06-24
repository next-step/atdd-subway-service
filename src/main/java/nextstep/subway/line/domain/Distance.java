package nextstep.subway.line.domain;

import nextstep.subway.exception.Message;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {

    private static final int MINIMUM_DISTANCE = 1;

    @Column(name = "distance")
    private int distance;

    protected Distance() {
    }

    private Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance < MINIMUM_DISTANCE) {
            throw new IllegalArgumentException(Message.ERROR_DISTANCE_TOO_SHORT_TO_BE_CREATED.showText());
        }
    }

    public static Distance ofValue(int distance) {
        return new Distance(distance);
    }

    public int getDistanceValue() {
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
        Distance compareDistance = (Distance) o;
        return distance == compareDistance.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }

    public boolean isSameOrShorterThan(Distance newDistance) {
        return distance <= newDistance.getDistanceValue();
    }

    public void minus(Distance newDistance) {
        this.distance -= newDistance.getDistanceValue();
    }

    public void plus(Distance newDistance) {
        this.distance += newDistance.getDistanceValue();
    }
}
