package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private int distance;

    public Distance() {
    }

    public Distance(final int distance) {
        this.distance = distance;
    }

    public int getValue() {
        return distance;
    }

    public boolean isEqualOrLessThan(final Distance newDistance) {
        return distance <= newDistance.getValue();
    }

    public void minus(final Distance newDistance) {
        distance = distance - newDistance.getValue();
    }
}
