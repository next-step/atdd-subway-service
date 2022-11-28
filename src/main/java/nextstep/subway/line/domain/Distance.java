package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.common.constant.ErrorCode;

@Embeddable
public class Distance {

    private static final int ZERO = 0;

    @Column(nullable = false)
    private int distance;

    protected Distance() {}

    private Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    public static Distance from(int distance) {
        return new Distance(distance);
    }

    private void validateDistance(int distance) {
        if(distance <= ZERO) {
            throw new IllegalArgumentException(ErrorCode.노선거리는_0보다_작거나_같을_수_없음.getErrorMessage());
        }
    }

    public Distance subtract(Distance distance) {
        return new Distance(this.distance - distance.distance);
    }

    public Distance add(Distance distance) {
        return new Distance(this.distance + distance.distance);
    }

    public boolean isBiggerThan(Distance distance) {
        return this.distance > distance.distance;
    }

    public int value() {
        return distance;
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
