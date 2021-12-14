package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.line.exception.DistanceTooLongException;
import nextstep.subway.line.exception.InvalidDistanceException;

@Embeddable
public class Distance {

    static int MINIMUM = 1;

    @Column
    private int distance;

    protected Distance() {
    }

    private Distance(final int distance) {
        validateGreaterThanOrEqualsMinimum(distance);
        this.distance = distance;
    }

    public static Distance of(final int distance) {
        return new Distance(distance);
    }

    public static Distance of(double distance) {
        return new Distance((int) distance);
    }

    public int getDistance() {
        return distance;
    }

    public Distance add(final Distance toAdd) {
        return Distance.of(distance + toAdd.distance);
    }

    public Distance subtract(final Distance toSubtract) {
        validateDistanceSubtractAble(toSubtract);
        return Distance.of(distance - toSubtract.distance);
    }

    private void validateDistanceSubtractAble(Distance newDistance) {
        if (distance <= newDistance.distance) {
            throw new DistanceTooLongException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    private void validateGreaterThanOrEqualsMinimum(int distance) {
        if (distance < MINIMUM) {
            throw new InvalidDistanceException();
        }
    }
}
