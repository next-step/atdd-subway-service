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

    protected Distance(int distance) {
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

    public boolean isSameOrShorterThan(Distance newDistance) {
        return distance <= newDistance.getDistanceValue();
    }

    public Distance minus(Distance newDistance) {
        if (isSameOrShorterThan(newDistance)) {
            throw new IllegalArgumentException(Message.ERROR_INPUT_DISTANCE_SHOULD_BE_LESS_THAN_EXISTING_DISTANCE.showText());
        }

        return new Distance(distance - newDistance.getDistanceValue());
    }

    public Distance plus(Distance newDistance) {
        return new Distance(distance + newDistance.getDistanceValue());
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
}
