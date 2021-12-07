package nextstep.subway.domain.line.domain;

import nextstep.subway.global.exception.SectionDistanceFewerThanMaximum;
import nextstep.subway.global.exception.SectionDistanceLessThanMinimumException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
}
