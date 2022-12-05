package nextstep.subway.line.domain;

import nextstep.subway.ErrorMessage;
import org.jgrapht.graph.DefaultWeightedEdge;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance extends DefaultWeightedEdge {

    private static int LOWER_LIMIT = 0;

    private int distance;

    public Distance(int distance) {
        if(distance <= LOWER_LIMIT) {
            throw new IllegalArgumentException(
                    ErrorMessage.INVALID_DISTANCE_VALUE.setLimitValueAndGetMessage(String.valueOf(LOWER_LIMIT)));
        }
        this.distance = distance;
    }

    protected Distance() {

    }

    public Distance subtract(Distance distance) {
        int newDistance = this.distance - distance.getDistance();
        if (newDistance <= LOWER_LIMIT) {
            throw new IllegalArgumentException(ErrorMessage.EXCEED_SECTION_DISTANCE.getMessage());
        }
        this.distance = newDistance;
        return this;
    }

    public Distance add(Distance distance) {
        this.distance += distance.getDistance();
        return this;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    protected double getWeight() {
        return (double) getDistance();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance that = (Distance) o;
        return distance == that.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }

}
