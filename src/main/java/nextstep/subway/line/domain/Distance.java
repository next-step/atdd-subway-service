package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance implements Comparable<Distance> {

    @Column(nullable = false)
    private int distance;

    private Distance() {
    }

    public Distance(int distance) {
        this.distance = distance;
    }

    public Distance add(Distance distance) {
        return new Distance(this.distance + distance.distance);
    }

    public Distance subtract(Distance distanceToSubtract) {
        return new Distance(this.distance - distanceToSubtract.distance);
    }

    public int getDistance() {
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

    @Override
    public int compareTo(Distance o) {
        return Integer.compare(this.distance, o.distance);
    }
}
