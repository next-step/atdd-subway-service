package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.common.exception.InvalidParameterException;

@Embeddable
public class Distance {

    @Column
    private int distance;

    protected Distance() {
    }

    protected Distance(Integer distance) {
        this.distance = distance;
    }

    public static Distance of(Integer distance) {
        return new Distance(distance);
    }

    public Integer getDistance() {
        return distance;
    }

    public void minus(Integer distance) {
        validGreaterThan(distance);

        this.distance -= distance;
    }

    public void plus(Distance distance) {
        this.distance += distance.distance;
    }

    private void validGreaterThan(Integer distance) {
        if (this.distance <= distance) {
            throw InvalidParameterException.DISTANCE_RANGE_EXCEPTION;
        }
    }
}
