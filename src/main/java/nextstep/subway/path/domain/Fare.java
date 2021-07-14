package nextstep.subway.path.domain;

import java.util.Objects;

public class Fare {

    public static final int OVER_FEE = 100;
    public static final int DEFAULT_FARE = 1250;
    public static final int MINIMUM_FARE = 0;

    public static final int ZERO_DISCOUNT = 0;
    public static final int TEENAGER_DISCOUNT_PERCENT = 20;
    public static final int CHILD_DISCOUNT_PERCENT = 50;
    public static final int PERCENTILE = 100;

    private final int fare;

    public Fare(int fare) {
        this.fare = fare;
    }

    public int calculateFare(int age, int distance) {
        int fare = calculateFareByDistance(distance);

        int discount = discountFareByAge(age);

        return fare - discount;
    }

    private int discountFareByAge(int age) {
        if (AgePolicy.isTeenager(age)) {
            return (TEENAGER_DISCOUNT_PERCENT * fare) / PERCENTILE;
        }

        if (AgePolicy.isChild(age)) {
            return (CHILD_DISCOUNT_PERCENT * fare) / PERCENTILE;
        }

        return ZERO_DISCOUNT;
    }

    private int calculateFareByDistance(int distance) {
        if (DistancePolicy.isNotAddedFareDistance(distance)) {
            return fare;
        }

        if (DistancePolicy.isFirstAddedFareDistance(distance)) {
            return fare + DistancePolicy.ADDED_DISTANCE_UNDER_MAXIMUM_BOUNDARY.calculateOverFare(distance);
        }

        return fare + DistancePolicy.ADDED_DISTANCE_UPON_MAXIMUM_BOUNDARY.calculateOverFare(distance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
