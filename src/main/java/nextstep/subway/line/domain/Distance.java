package nextstep.subway.line.domain;

import java.util.*;

import javax.persistence.*;

@Embeddable
public class Distance {
    private static final String DISTANCE_SHOULD_BE_GREATER_EQUAL_THAN_ZERO_EXCEPTION_STATEMENT = "거리 값은 0이상이어야 합니다.";
    private static final int ZERO = 0;
    private int distance;

    public Distance() {

    }

    private Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    public static Distance from(int distance) {
        return new Distance(distance);
    }

    private void validate(int distance) {
        if (distance < ZERO) {
            throw new IllegalArgumentException(DISTANCE_SHOULD_BE_GREATER_EQUAL_THAN_ZERO_EXCEPTION_STATEMENT);
        }
    }

    public Distance add(Distance distance) {
        return Distance.from(this.distance + distance.distance);
    }

    public Distance subtract(Distance distance) {
        validate(this.distance - distance.distance);
        return Distance.from(this.distance - distance.distance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Distance))
            return false;
        Distance distance1 = (Distance)o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
  
    public int distance() {
        return distance;
    }
}
