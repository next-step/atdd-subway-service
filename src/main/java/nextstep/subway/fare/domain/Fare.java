package nextstep.subway.fare.domain;

import java.util.Objects;

public class Fare {
    private int value;

    public Fare(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Fare of(int baseFare) {
        return new Fare(baseFare);
    }

    public Fare addExtraOf(int distance) {
        Fare result = this;
        for (DistanceExtraFare distanceExtraFare : DistanceExtraFare.values()) {
            result = result.plus(getExtraFare(distance, distanceExtraFare));
        }

        return result;
    }

    private Fare plus(Fare extra) {
        return new Fare(this.value + extra.value);
    }

    private Fare getExtraFare(int distance, DistanceExtraFare distanceExtraFare) {
        if (distance < distanceExtraFare.getFrom()) {
            return new Fare(0);
        }
        int extraDistance = Math.min(distance, distanceExtraFare.getTo()) - distanceExtraFare.getFrom() + 1;
        return new Fare(calculateExtraFare(extraDistance, distanceExtraFare));
    }

    private int calculateExtraFare(int additionalDistance, DistanceExtraFare distanceExtraFare) {
        return (int) ((Math.ceil((additionalDistance - 1) / distanceExtraFare.getUnitDistance()) + 1) * distanceExtraFare.getUnitExtra());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Fare{" +
                "value=" + value +
                '}';
    }
}
