package nextstep.subway.line.domain;

import nextstep.subway.line.exception.TooLongDistanceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    public static final int SHORT_DISTANCE = 10;
    public static final int MEDIUM_DISTANCE = 50;

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

    public static Distance of(double distance) {
        return new Distance((int) distance);
    }

    public static Distance ofMaxMedium() {
        return new Distance(MEDIUM_DISTANCE);
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

    public boolean isShortRange() {
        return this.distance <= SHORT_DISTANCE;
    }

    public boolean isMediumRange() {
        return this.distance > SHORT_DISTANCE && this.distance <= MEDIUM_DISTANCE;
    }

    public boolean isLargeRange() {
        return this.distance > MEDIUM_DISTANCE;
    }

    public int intValue() {
        return this.distance;
    }
}
