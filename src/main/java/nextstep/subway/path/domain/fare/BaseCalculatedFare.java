package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.fare.discount.Discount;

public class BaseCalculatedFare implements CalculatedFare {
    private static final int ADULT_BASE_FARE = 1_250;

    private final Discount discount;

    BaseCalculatedFare(Discount discount) {
        this.discount = discount;
    }

    @Override
    public int calculateFare() {
        return discount.discount(ADULT_BASE_FARE);
    }
}
