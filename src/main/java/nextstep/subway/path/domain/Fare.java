package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.SurCharge;

public class Fare {

    private static final int DEFAULT_FARE = 1250;
    private static final int MIN_DISTANCE_STANDARD = 10;
    private static final int MAX_DISTANCE_STANDARD = 50;
    private static final int UNIT_DISTANCE_BETWEEN_TEN_AND_FIFTY = 5;
    private static final int UNIT_DISTANCE_OVER_FIFTY = 8;

    private int fare;

    public Fare(Distance distance, SurCharge surCharge, int age) {
        fare = calculateFare(distance, surCharge, age);
    }

    private int calculateFare(Distance distance, SurCharge surCharge, int age) {
        int fareByDistance = adjustFarePolicyByDistance(distance) + surCharge.value();
        double discountFareByAge = adjustFarePolicyByAge(fareByDistance, age);
        return (int) discountFareByAge;
    }

    private int adjustFarePolicyByDistance(Distance distance) {
        int fareByDistance = DEFAULT_FARE;

        if (distance.value() > MIN_DISTANCE_STANDARD && distance.value() < MAX_DISTANCE_STANDARD) {
            fareByDistance += calculateOverFare(distance.value()-MIN_DISTANCE_STANDARD, UNIT_DISTANCE_BETWEEN_TEN_AND_FIFTY);
        }

        if (distance.value() >= MAX_DISTANCE_STANDARD) {
            fareByDistance += calculateOverFare(MAX_DISTANCE_STANDARD - MIN_DISTANCE_STANDARD, UNIT_DISTANCE_BETWEEN_TEN_AND_FIFTY);
            fareByDistance += calculateOverFare(distance.value()- MAX_DISTANCE_STANDARD, UNIT_DISTANCE_OVER_FIFTY);
        }

        return fareByDistance;
    }

    private double adjustFarePolicyByAge(int fare, int age) {
        if (age >= DiscountPolicyByAge.CHILDREN_DISCOUNT.getMinAge()
                && age <= DiscountPolicyByAge.CHILDREN_DISCOUNT.getMaxAge()) {
            return (fare - DiscountPolicyByAge.CHILDREN_DISCOUNT.getDeduction()) * DiscountPolicyByAge.CHILDREN_DISCOUNT.getPriceRate();
        }

        if (age >= DiscountPolicyByAge.YOUTH_DISCOUNT.getMinAge() && age <= DiscountPolicyByAge.YOUTH_DISCOUNT.getMaxAge()) {
            return (fare - DiscountPolicyByAge.YOUTH_DISCOUNT.getDeduction()) * DiscountPolicyByAge.YOUTH_DISCOUNT.getPriceRate();
        }

        return fare;
    }

    private int calculateOverFare(int distance, int standard) {
        return (int) ((Math.ceil((distance - 1) / standard) + 1) * 100);
    }

    public int value() {
        return fare;
    }
}
