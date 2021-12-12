package nextstep.subway.domain.line.domain;

import nextstep.subway.domain.line.exception.SectionDistanceFewerThanMaximum;
import nextstep.subway.domain.line.exception.SectionDistanceLessThanMinimumException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {

    private static final int MINIMUM_DISTANCE = 1;

    @Column
    private int distance;

    public Distance() {
    }

    public Distance(final int distance) {
        if (distance < MINIMUM_DISTANCE) {
            throw new SectionDistanceLessThanMinimumException();
        }
        this.distance = distance;
    }

    public Distance plus(int newDistance) {
        return new Distance(this.getDistance() + newDistance);
    }

    public Distance plus(Distance newDistance) {
        return plus(newDistance.distance);
    }

    public Distance minus(Distance newDistance) {
        return minus(newDistance.distance);
    }

    public Distance minus(int newDistance) {
        if (this.distance <= newDistance) {
            throw new SectionDistanceFewerThanMaximum();
        }
        return new Distance(this.distance - newDistance);
    }

    public int getDistance() {
        return this.distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
