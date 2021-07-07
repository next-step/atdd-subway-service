package nextstep.subway.path.domain.fare;

import java.util.Arrays;

public enum FareOfDistancePolicyFactory {

    BASE_RATE(new BaseFareOfDistanceCalculator()),
    FIRST_ADDITIONAL_FARE(new FirstAdditionalFareOfDistanceCalculator()),
    SECOND_ADDITIONAL_FARE(new SecondAddtionalFareOfDistanceCalculator());

    private FareOfDistanceCalculator fareOfDistanceCalculator;

    FareOfDistancePolicyFactory(final FareOfDistanceCalculator fareOfDistanceCalculator) {
        this.fareOfDistanceCalculator = fareOfDistanceCalculator;
    }

    public static int calculate(final int distance) {
        return Arrays.stream(values())
                     .map(policy -> policy.acceptPolicy(distance))
                     .mapToInt(value -> value)
                     .sum();
    }

    private int acceptPolicy(final int distance) {
        return this.fareOfDistanceCalculator.calculate(distance);
    }
}
