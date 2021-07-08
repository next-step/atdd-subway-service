package nextstep.subway.path.domain;

import nextstep.subway.enums.SubwayFarePolicy;

import java.math.BigDecimal;
import java.util.Objects;

public class Fare implements Comparable<Fare> {
    private static final int BASE_FARE = 1250;
    private static final int BASE_DISTANCE = 10;
    private static final int FARE_DISTANCE_BOUNDARY = 50;

    private int fare;

    public Fare() {
    }

    public Fare(int fare) {
        this.fare = fare + BASE_FARE;
    }

    public int calcSubwayFare(int totalDistance, SubwayFarePolicy farePolicy) {
        if (totalDistance <= BASE_DISTANCE) {
            return new BigDecimal(1 - farePolicy.getDiscountRate()).multiply(new BigDecimal(fare)).intValue();
        }
        int overFare = calcOverFare(totalDistance);
        return new BigDecimal(1 - farePolicy.getDiscountRate()).multiply(new BigDecimal(fare + overFare)).intValue();
    }

    private int calcOverFare(int distance) {
        FareBoundaryCalculator fareBoundaryCalculator = new FareBoundaryCalculator();
        if (distance > FARE_DISTANCE_BOUNDARY) {
            int boundaryOverFare =  new FareBoundaryOverCalculator().calcOverFare(distance - FARE_DISTANCE_BOUNDARY);
            return fareBoundaryCalculator.calcOverFare(FARE_DISTANCE_BOUNDARY - BASE_DISTANCE) + boundaryOverFare;
        }
        return fareBoundaryCalculator.calcOverFare(distance - BASE_DISTANCE);
    }

    @Override
    public int compareTo(Fare o) {
        return fare - o.fare;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Fare fare = (Fare) object;
        return this.fare == fare.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
