package nextstep.subway.path.domain.fare;

import java.util.Arrays;

public enum FareOfDistancePolicy {

    BASE_RATE(new BaseFareCalculator()),
    FIRST_ADDITIONAL_FARE(new FirstAdditionalFareCalculator()),
    SECOND_ADDITIONAL_FARE(new SecondAddtionalFareCalculator());

    private FareCalculator fareCalculator;

    FareOfDistancePolicy(final FareCalculator fareCalculator) {
        this.fareCalculator = fareCalculator;
    }

    public static int calculate(final int distance) {
        return Arrays.stream(values())
                     .map(policy -> policy.acceptPolicy(distance))
                     .mapToInt(value -> value)
                     .sum();
    }

    private int acceptPolicy(final int distance) {
        return this.fareCalculator.calculate(distance);
    }
}
