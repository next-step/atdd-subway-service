package nextstep.subway.line.domain;

import nextstep.subway.line.exception.TooLongDistanceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    @Column
    private int distance;

    protected Distance() {
    }

    private Distance(int distance) {
        this.distance = distance;
    }

    public static Distance of(int distance) {
        return new Distance(distance);
    }

    public static Distance of(int distance1, int distance2) {
        return new Distance(distance1 + distance2);
    }

    public Distance minus(int distance) {
        if (isLessThanOrEqualTo(distance)) {
            throw new TooLongDistanceException();
        }
        return Distance.of(this.distance - distance);
    }

    private boolean isLessThanOrEqualTo(int distance) {
        return this.distance <= distance;
    }

    public boolean match(int distance) {
        return this.distance == distance;
    }

    public int getDistance() {
        return distance;
    }
}
