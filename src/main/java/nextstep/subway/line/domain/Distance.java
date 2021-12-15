package nextstep.subway.line.domain;

import nextstep.subway.line.exception.OutOfDistanceRangeException;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final int ZERO = 0;
    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        validDistance(distance);
        this.distance = distance;
    }

    private void validDistance(int distance) {
        if (distance <= ZERO) {
            throw new OutOfDistanceRangeException("거리는 0보다 커야 합니다.");
        }
    }

    public Distance minus(Distance distance) {
        return new Distance(this.distance - distance.distance);
    }

    public Distance plus(Distance distance) {
        return new Distance(this.distance + distance.distance);
    }

    public boolean isShorter(Distance newDistance) {
        return this.distance <= newDistance.distance;
    }
}
