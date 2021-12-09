package nextstep.subway.line.domain;


import javax.persistence.Embeddable;
import java.util.Objects;

import static nextstep.subway.line.application.exception.InvalidSectionException.*;

@Embeddable
public class Distance {
    protected static final int MIN_DISTANCE = 1;

    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance < MIN_DISTANCE) {
            throw shorterThanMinDistance(MIN_DISTANCE);
        }
    }

    public boolean divisible(Distance divideDistance) {
        if (distance <= divideDistance.getDistance()) {
            throw LONGER_THAN_BETWEEN_SECTIONS;
        }
        return true;
    }

    public Distance plus(Distance newDistance) {
        return new Distance(this.distance + newDistance.distance);
    }

    public Distance minus(Distance newDistance) {
        return new Distance(this.distance - newDistance.distance);
    }

    private void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
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
