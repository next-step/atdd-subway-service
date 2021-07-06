package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.fare.discount.Discount;

public class SecondAdditionalCalculatedFare implements CalculatedFare {
    private static final int DISTANCE_UNTIL_FIRST_ADDITIONAL_FARE = 50;
    private static final int FARE_ADDITIONAL_DISTANCE = 8;
    private static final int ADDITIONAL_FARE_UNIT = 100;

    private final FirstAdditionalCalculatedFare firstAdditionalFare;
    private final int secondAdditionalSectionDistance;
    private final Discount discount;

    SecondAdditionalCalculatedFare(int distance, Discount discount) {
        if (distance <= DISTANCE_UNTIL_FIRST_ADDITIONAL_FARE) {
            throw new IllegalArgumentException();
        }
        this.firstAdditionalFare = new FirstAdditionalCalculatedFare(DISTANCE_UNTIL_FIRST_ADDITIONAL_FARE, discount);
        this.secondAdditionalSectionDistance = distance - DISTANCE_UNTIL_FIRST_ADDITIONAL_FARE;
        this.discount = discount;
    }

    @Override
    public int calculateFare() {
        return firstAdditionalFare.calculateFare()
                + (int) Math.ceil((secondAdditionalSectionDistance) / FARE_ADDITIONAL_DISTANCE)
                * discount.discount(ADDITIONAL_FARE_UNIT);
    }
}
