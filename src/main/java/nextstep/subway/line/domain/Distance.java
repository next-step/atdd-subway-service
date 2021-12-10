package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.common.exception.CommonErrorCode;
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
            throw InvalidParameterException.of(CommonErrorCode.DISTANCE_RANGE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
