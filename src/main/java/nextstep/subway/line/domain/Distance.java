package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    @Column
    private int distance;

    protected Distance() {
    }

    private Distance(final int distance) {
        this.distance = distance;
    }

    public static Distance of(final int distance) {
        return new Distance(distance);
    }

    public int getDistance() {
        return distance;
    }

    public void subtract(final int newDistance) {
        this.distance -= newDistance;
    }
}
