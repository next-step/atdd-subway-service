package nextstep.subway.line.domain;

import nextstep.subway.common.exception.RegisterDistanceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final int INT_ZERO = 0;

    @Column
    private int distance;

    protected Distance() {
    }

    private Distance(int distance) {
        this.distance = distance;
    }

    public static Distance of(int distance) {
        validate(distance);
        return new Distance(distance);
    }

    public Distance minus(Distance targetDistance) {
        if (isLessThanOrEqualTo(targetDistance)) {
            throw new RegisterDistanceException();
        }
        return Distance.of(this.distance - targetDistance.distance);
    }

    public Distance plus(Distance targetDistance) {
        return Distance.of(this.distance + targetDistance.distance);
    }

    private static void validate(int distance) {
        if(distance <= INT_ZERO) {
            throw new RegisterDistanceException();
        }
    }

    private boolean isLessThanOrEqualTo(Distance targetDistance) {
        return this.distance <= targetDistance.distance;
    }

    public int getDistance() {
        return distance;
    }
}
