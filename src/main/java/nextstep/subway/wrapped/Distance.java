package nextstep.subway.wrapped;

import javax.persistence.Embeddable;
import java.util.Objects;

import static java.lang.String.format;

@Embeddable
public class Distance {
    private static final int MINIMUM_DISTANCE = 0;

    private int distance;

    protected Distance() {
    }

    public Distance(double weight) {
        this((int) weight);
    }

    public Distance(int distance) {
        validate(distance);

        this.distance = distance;
    }

    public boolean isLessThen(Distance distance) {
        return this.distance <= distance.distance;
    }

    public Distance minus(Distance distance) {
        return new Distance(this.distance - distance.distance);
    }

    public Distance plus(Distance distance) {
        return new Distance(this.distance + distance.distance);
    }

    public Distance divide(Distance distance) {
        return new Distance(this.distance / distance.distance);
    }

    private void validate(int distance) {
        if (distance < MINIMUM_DISTANCE) {
            throw new IllegalArgumentException(format("최소 거리는 %d 이상이여야 합니다", MINIMUM_DISTANCE));
        }
    }

    public int toInt() {
       return this.distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }

    public int compareTo(Distance l2Distance) {
        return Long.compare(this.distance, l2Distance.distance);
    }
}
