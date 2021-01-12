package nextstep.subway.fare;

import java.util.Arrays;
import java.util.function.Function;

public enum Passenger {
    INFANT(0, 5, 100, fare -> Fare.of(0)),
    CHILD(6, 12, 50, Passenger::fareMinusBaseDeduction),
    ADOLESCENT(13, 18, 20, Passenger::fareMinusBaseDeduction),
    ADULT(19, 200, 0, fare -> fare);

    private static final int BASE_DEDUCTION_AMOUNT = 350;
    private final int startingAgeRange;
    private final int endingAgeRange;
    private final int deduction_percentage;
    private final Function<Fare, Fare> fareExpression;

    Passenger(final int startingAgeRange, final int endingAgeRange, final int deduction_percentage, final Function<Fare, Fare> fareExpression) {
        this.startingAgeRange = startingAgeRange;
        this.endingAgeRange = endingAgeRange;
        this.deduction_percentage = deduction_percentage;
        this.fareExpression = fareExpression;
    }

    public static Passenger getPassengerType(final int passengerAge) {
        return Arrays.stream(values()).sorted()
            .filter(passenger -> passenger.startingAgeRange <= passengerAge)
            .filter(passenger -> passenger.endingAgeRange >= passengerAge)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    private static Fare fareMinusBaseDeduction(final Fare fare) {
        return fare.minus(Passenger.BASE_DEDUCTION_AMOUNT);
    }

    public Fare discountByPassengerType(final Fare fare) {
        return this.fareExpression.apply(fare).discountByPercentage(this.deduction_percentage);
    }
}
