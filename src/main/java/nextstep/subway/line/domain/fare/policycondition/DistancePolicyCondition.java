package nextstep.subway.line.domain.fare.policycondition;


import nextstep.subway.line.domain.Money;

public interface DistancePolicyCondition {

    default Money calculate(int distance, int minDistance, int premiumDistance,
        int additionalFare) {
        return Money.won(
            (int) ((Math.floor((double) (distance - minDistance - 1) / premiumDistance) + 1)
                * additionalFare));
    }

    Money calculateFare(int distance);
}
