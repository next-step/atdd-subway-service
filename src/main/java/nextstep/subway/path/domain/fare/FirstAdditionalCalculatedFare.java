package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.fare.discount.Discount;

public class FirstAdditionalCalculatedFare implements CalculatedFare {
    private static final int DISTANCE_UNTIL_BASE_FARE = 10;
    private static final int FARE_ADDITIONAL_DISTANCE = 5;
    private static final int ADDITIONAL_FARE_UNIT = 100;

    private final BaseCalculatedFare baseCalculatedFare;
    private final int firstAdditionalSectionDistance;
    private final Discount discount;

    FirstAdditionalCalculatedFare(int distance, Discount discount) {
        if (distance <= DISTANCE_UNTIL_BASE_FARE) {
            throw new IllegalArgumentException();
        }
        this.baseCalculatedFare = new BaseCalculatedFare(discount);
        this.firstAdditionalSectionDistance = distance - DISTANCE_UNTIL_BASE_FARE;
        this.discount = discount;
    }

    @Override
    public int calculateFare() {
        return baseCalculatedFare.calculateFare()
                + (int) (Math.ceil((firstAdditionalSectionDistance) / FARE_ADDITIONAL_DISTANCE) + 1)
                * discount.discount(ADDITIONAL_FARE_UNIT);
    }
}
