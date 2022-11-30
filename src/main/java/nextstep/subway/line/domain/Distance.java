package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

import static nextstep.subway.exception.ErrorMessage.DISTANCE_BETWEEN_STATION_OVER;
import static nextstep.subway.exception.ErrorMessage.DISTANCE_CANNOT_BE_ZERO;

@Embeddable
public class Distance {

    private int distance;

    protected Distance() {

    }

    public Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException(DISTANCE_CANNOT_BE_ZERO.getMessage());
        }
    }


    public void minus(Distance distance) {
        if (this.distance <= distance.distance) {
            throw new IllegalArgumentException(DISTANCE_BETWEEN_STATION_OVER.getMessage());
        }
        this.distance -= distance.distance;
    }

    public void plus(int distance) {
        this.distance += distance;
    }

    public int value() {
        return distance;
    }
}
