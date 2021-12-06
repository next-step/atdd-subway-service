package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import nextstep.subway.exception.NotValidateException;

@Embeddable
public final class Distance {
    private static final int DISTANCE_MIN_NUMBER = 0;

    private Integer distance;

    protected Distance() {
    }

    private Distance(Integer distance) {
        validate(distance);
        this.distance = distance;
    }

    public static Distance valueOf(Integer distance) {
        return new Distance(distance);
    }

    public Distance minus(Distance other) {
        return valueOf(this.distance - other.distance);
    }

    public Integer get() {
        return distance;
    }

    public Distance plus(Distance other) {
        return valueOf(this.distance + other.distance);
    }

    /**
     * positive number
     *
     * @param distance
     */
    private void validate(Integer distance) {
        if (distance <= DISTANCE_MIN_NUMBER) {
            throw new NotValidateException(DISTANCE_MIN_NUMBER+" 이상의 정수만 입력가능합니다.");
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
        Distance other = (Distance) o;
        return Objects.equals(distance, other.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }


    @Override
    public String toString() {
        return "Distance{" +
            "distance=" + distance +
            '}';
    }
}
