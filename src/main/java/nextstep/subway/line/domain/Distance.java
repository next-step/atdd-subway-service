package nextstep.subway.line.domain;

import nextstep.subway.common.exception.RegisterDistanceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    private static final int INT_ZERO = 0;

    @Column
    private int distance;

    protected Distance() {
    }

    private Distance(final int distance) {
        this.distance = distance;
    }

    public static Distance of(final int distance) {
        validate(distance);
        return new Distance(distance);
    }

    public Distance minus(final Distance targetDistance) {
        if (isLessThanOrEqualTo(targetDistance)) {
            throw new RegisterDistanceException();
        }
        return Distance.of(this.distance - targetDistance.distance);
    }

    public Distance plus(final Distance targetDistance) {
        return Distance.of(this.distance + targetDistance.distance);
    }

    private static void validate(final int distance) {
        if(distance <= INT_ZERO) {
            throw new RegisterDistanceException();
        }
    }

    private boolean isLessThanOrEqualTo(final Distance targetDistance) {
        return this.distance <= targetDistance.distance;
    }

    public int getDistance() {
        return distance;
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
