package nextstep.subway.line.domain;

import nextstep.subway.exception.NotValidDataException;
import nextstep.subway.exception.type.ValidExceptionType;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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

    public boolean isOverDistance(int distance) {
        return this.distance <= distance;
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
