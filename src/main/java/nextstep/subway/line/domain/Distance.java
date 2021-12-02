package nextstep.subway.line.domain;

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

    public static Distance valueOf(DistanceType type, Distance src, Distance target) {
        if (type.isPlus()) {
            return Distance.of(src.distance + target.distance);
        }

        if (type.isMinus()) {
            return Distance.of(src.distance - target.distance);
        }
        return null;
    }

    public static Distance of(int distance) {
        return new Distance(distance);
    }

    private void validate(int distance) {
        if (distance < MIN_DISTANCE) {
            throw new IllegalDistanceException();
        }
    }

    public boolean isLessThanOrEqualTo(Distance newDistance) {
        return distance <= newDistance.distance;
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
