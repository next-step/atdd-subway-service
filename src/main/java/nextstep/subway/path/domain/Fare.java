package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Surcharge;

public class Fare {
    private static final int DEFAULT_FARE = 1250;
    private int fare;

    public Fare(Distance distance, Surcharge surcharge, int age) {
        fare = calculateFare(distance, surcharge, age);
    }

    private int calculateFare(Distance distance, Surcharge surcharge, int age) {
        int fareByDistance = adjustFarePolicyByDistance(distance.value()) + surcharge.value();
        double discountFareByAge = adjustFarePolicyByAge(fareByDistance, age);
        return (int) discountFareByAge;
    }

    private int adjustFarePolicyByDistance(int distance) {
        int fareByDistance = DEFAULT_FARE;

        if (distance > FarePolicyByDistance.POLICY_UNDER_FIFTY.getMinDistanceStandard()
                && distance < FarePolicyByDistance.POLICY_OVER_FIFTY.getMinDistanceStandard()) {
            fareByDistance += calculateOverFare(distance - FarePolicyByDistance.POLICY_UNDER_FIFTY.getMinDistanceStandard(),
                    FarePolicyByDistance.POLICY_UNDER_FIFTY.getUnitDistanceForCharge());
        }

        if (distance >= FarePolicyByDistance.POLICY_OVER_FIFTY.getMinDistanceStandard()) {
            fareByDistance += calculateOverFare(FarePolicyByDistance.POLICY_OVER_FIFTY.getMinDistanceStandard() - FarePolicyByDistance.POLICY_UNDER_FIFTY.getMinDistanceStandard(),
                    FarePolicyByDistance.POLICY_UNDER_FIFTY.getUnitDistanceForCharge());
            fareByDistance += calculateOverFare(distance - FarePolicyByDistance.POLICY_OVER_FIFTY.getMinDistanceStandard(),
                    FarePolicyByDistance.POLICY_OVER_FIFTY.getUnitDistanceForCharge());
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
