package nextstep.subway.line.domain;

import nextstep.subway.exception.NotValidDataException;
import nextstep.subway.exception.type.ValidExceptionType;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static nextstep.subway.exception.type.ValidExceptionType.NOT_SHORT_VALID_DISTANCE;

@Embeddable
public class Distance {

    private static final int SIZE_ZERO = 0;
    @Column(nullable = false)
    private int distance;

    protected Distance() {}

    private Distance(int distance) {
        validCheckMinDistanceSize(distance);
        this.distance = distance;
    }

    public void validCheckOverDistance(int distance) {
        if (this.distance <= distance) {
            throw new NotValidDataException(NOT_SHORT_VALID_DISTANCE.getMessage());
        }
    }

    public void validCheckMinDistanceSize(int distance) {
        if (distance <= SIZE_ZERO) {
            throw new NotValidDataException(ValidExceptionType.NOT_ZERO_VALID_DISTANCE.getMessage());
        }
    }


    public Distance minus(int distance) {
        return Distance.from(this.distance - distance);
    }

    public static Distance from(int distance) {
        return new Distance(distance);
    }

    public Distance plus(Distance distance) {
        return Distance.from(this.distance + distance.distance);
    }

    public int getDistance() {
        return distance;
    }
}
