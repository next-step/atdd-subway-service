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

    public boolean isEqualOrLessThan(final Distance other) {
        return distance <= other.getValue();
    }

    public void minus(final Distance other) {
        distance = distance - other.getValue();
    }
}
