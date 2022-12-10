package nextstep.subway.path.domain.fare;

import nextstep.subway.line.domain.Distance;

public class DistanceFareStrategy {

    private static final int BASE_POINT = 10;
    private static final int MIDDLE_POINT = 50;
    private static final int MIDDLE_CHARGE_DISTANCE = 5;
    private static final int LAST_CHARGE_DISTANCE = 8;
    private static final int ADDITIONAL_CHARGE = 100;

    private Distance distance;

    public DistanceFareStrategy(Distance distance) {
        this.distance = distance;
    }

    public int getAdditionalFare() {
        if (this.distance.value() <= BASE_POINT) {
            return 0;
        }
        if (BASE_POINT < distance.value() && distance.value() <= MIDDLE_POINT) {
            return additionalChargeInMiddleSection(distance.value());
        }
        return additionalChargeIFinalSection(distance.value());
    }

    private int additionalChargeInMiddleSection(int distance) {
        return (distance - BASE_POINT) / MIDDLE_CHARGE_DISTANCE * ADDITIONAL_CHARGE;
    }

    private int additionalChargeIFinalSection(int distance) {
        return (MIDDLE_POINT - BASE_POINT) / MIDDLE_CHARGE_DISTANCE * ADDITIONAL_CHARGE
                + (distance - MIDDLE_POINT) / LAST_CHARGE_DISTANCE * ADDITIONAL_CHARGE;
    }
}
