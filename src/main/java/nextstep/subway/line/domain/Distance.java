package nextstep.subway.line.domain;

import nextstep.subway.line.message.DistanceMessage;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {

    private final Integer distance;

    protected Distance() {
        distance = null;
    }

    public Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if(distance < 1) {
            throw new IllegalArgumentException(DistanceMessage.CREATE_ERROR_MORE_THAN_ONE.message());
        }
    }

    public Distance minus(Distance other) {
        return new Distance(this.distance - other.distance);
    }

    public Distance plus(Distance other) {
        return new Distance(this.distance + other.distance);
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
        return distance != null ? distance.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.valueOf(distance);
    }
}
