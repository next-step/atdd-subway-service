package nextstep.subway.line.domain;

import nextstep.subway.line.exception.DistanceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    @Column(nullable = false)
    public int distance;

    public Distance() {
    }

    public Distance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public void minus(int newDistance) {
        if (distance <= newDistance) {
            throw new DistanceException();
        }
        distance -= newDistance;
    }
}
