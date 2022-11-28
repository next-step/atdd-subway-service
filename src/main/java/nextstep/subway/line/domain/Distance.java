package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    @Column
    private int distance;

    public Distance() {
    }

    private Distance(int value) {
        this.distance = value;
    }

    public static Distance from(int value){
        return new Distance(value);
    }

    public int getDistance() {
        return distance;
    }

    public void plusDistance(Distance otherDistance){
        this.distance += otherDistance.getDistance();
    }

    public void minusDistance(Distance newDistance){
        this.distance -= newDistance.getDistance();
    }

    public boolean isGreaterThan(int newDistance){
        return this.distance <= newDistance;
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
