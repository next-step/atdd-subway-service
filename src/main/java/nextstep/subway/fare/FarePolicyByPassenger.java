package nextstep.subway.fare;

import java.util.Arrays;
import java.util.function.Function;

public enum FarePolicyByPassenger {
    INFANT(0, 5, 100, fare -> Fare.of(0)),
    CHILD(6, 12, 50, FarePolicyByPassenger::fareMinusBaseDeduction),
    ADOLESCENT(13, 18, 20, FarePolicyByPassenger::fareMinusBaseDeduction),
    ADULT(19, 200, 0, fare -> fare);

    private static final int BASE_DEDUCTION_AMOUNT = 350;
    private final int startingAgeRange;
    private final int endingAgeRange;
    private final int deductionPercentage;
    private final Function<Fare, Fare> fareExpression;

    FarePolicyByPassenger(final int startingAgeRange, final int endingAgeRange, final int deductionPercentage, final Function<Fare, Fare> fareExpression) {
        this.startingAgeRange = startingAgeRange;
        this.endingAgeRange = endingAgeRange;
        this.deductionPercentage = deductionPercentage;
        this.fareExpression = fareExpression;
    }

    public static FarePolicyByPassenger getPassengerType(final int passengerAge) {
        return Arrays.stream(values()).sorted()
            .filter(farePolicyByPassenger -> farePolicyByPassenger.startingAgeRange <= passengerAge)
            .filter(farePolicyByPassenger -> farePolicyByPassenger.endingAgeRange >= passengerAge)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    private static Fare fareMinusBaseDeduction(final Fare fare) {
        return fare.minus(FarePolicyByPassenger.BASE_DEDUCTION_AMOUNT);
    }

    public Fare discountByPassengerType(final Fare fare) {
        return this.fareExpression.apply(fare).discountByPercentage(this.deductionPercentage);
    }
}
