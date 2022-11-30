package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.exception.DistanceException;

@Embeddable
public class Distance {

    private static final int MIN = 1;

    @Column(nullable = false)
    private int distance;

    protected Distance() {
    }

    private Distance(int distance) {
        this.distance = distance;
    }

    public static Distance from(int distance) {
        if (distance < MIN) {
            throw new DistanceException(MIN);
        }
        return new Distance(distance);
    }

    public Distance substract(Distance distance) {
        return Distance.from(this.distance - distance.value());
    }

    public Distance add(Distance distance) {
        return Distance.from(this.distance + distance.value());
    }

    public int value() {
        return this.distance;
    }
}