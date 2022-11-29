package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import nextstep.subway.line.exception.IllegalDistanceException;

@Embeddable
public class Distance {

    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        this.distance = distance;
    }

    public Distance minus(final Distance newDistance) {
        if (this.distance <= newDistance.distance) {
            throw new IllegalDistanceException();
        }
        return new Distance(this.distance - newDistance.distance);
    }

    public Distance plus(Distance newDistance) {
        return new Distance(this.distance + newDistance.distance);
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
