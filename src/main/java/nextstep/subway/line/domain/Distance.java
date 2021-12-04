package nextstep.subway.line.domain;

import nextstep.subway.common.exception.distance.DistanceNotAllowException;
import nextstep.subway.common.exception.distance.IllegalDistanceException;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : Distance
 * author : haedoang
 * date : 2021-12-01
 * description :
 */
@Embeddable
public class Distance {
    @Transient
    public static final int MIN_DISTANCE = 1;

    private Integer distance;

    protected Distance() {
    }

    private Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    public static Distance of(int distance) {
        return new Distance(distance);
    }

    public void checkAddSection(Section newSection) {
        if (this.distance <= newSection.getDistance().distance) {
            throw new DistanceNotAllowException();
        }
    }

    private void validate(int distance) {
        if (distance < MIN_DISTANCE) {
            throw new IllegalDistanceException();
        }
    }

    public boolean isLessThanOrEqualTo(Distance newDistance) {
        return distance <= newDistance.distance;
    }

    public Distance plus(Distance newDistance) {
        return Distance.of(distance + newDistance.distance);
    }

    public Distance minus(Distance newDistance) {
        return Distance.of(distance - newDistance.distance);
    }

    public int intValue() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance1 = (Distance) o;
        return Objects.equals(distance, distance1.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
