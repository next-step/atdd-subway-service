package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final int MINIMUM_DISTANCE = 1;
    private int distance;

    public Distance(int distance) {
        this.distance = distance;
    }

    protected Distance() {
    }

    public int getValue() {
        return distance;
    }

    public Distance minus(int distanceToMinus) {
        validateNewDistance(distance - distanceToMinus);
        return new Distance(distance - distanceToMinus);
    }

    public Distance plus(int distanceToPlus) {
        validateNewDistance(distance + distanceToPlus);
        return new Distance(distance + distanceToPlus);
    }

    private void validateNewDistance(int newDistance) {
        if (newDistance < MINIMUM_DISTANCE) {
            throw new RuntimeException("거리는 최소 1이상이어야 합니다.");
        }
    }
}
