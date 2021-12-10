package nextstep.subway.line.domain;

import static nextstep.subway.common.Message.MESSAGE_SECTION_DISTANCE_NOT_LESS_THAN_ZERO;

import java.util.Objects;

public class Distance {

    private double distance;

    public Distance(double distance) {
        validationArgument(distance);
        this.distance = distance;
    }

    public Distance(int distance) {
        validationArgument(distance);
        this.distance = distance;
    }

    private void validationArgument(double distance) {
        if (distance < 0) {
            throw new IllegalArgumentException(
                MESSAGE_SECTION_DISTANCE_NOT_LESS_THAN_ZERO.getMessage());
        }
    }

    public int getDistance() {
        return (int) distance;
    }

    public boolean lessThenOrEquals(Distance distance) {
        return distance.getDistance() <= this.distance;
    }

    public boolean greaterThan(Distance distance) {
        return distance.getDistance() > this.distance;
    }

    public Distance subtract(Distance distance) {
        final double result = this.distance - distance.getDistance();
        return new Distance(result);
    }

    public Distance divide(int val) {
        final double result = this.distance / val;
        return new Distance(result);
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
        return Double.compare(distance1.distance, distance) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }

    @Override
    public String toString() {
        return String.valueOf(distance);
    }
}
