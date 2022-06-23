package nextstep.subway.path.util;

import nextstep.subway.path.types.ChargeType;

public class ChargeCalculator {
    private static int DEFAULT_CHARGE = 1_250;

    public static int calculate(final int distance) {
        return calculateDistanceCharge(distance);
    }

    private static int calculateDistanceCharge(final int distance) {
        final ChargeType chargeType = ChargeType.of(distance);
        if (chargeType == ChargeType.LESS_OR_EQUAL_TEN) {
            return DEFAULT_CHARGE;
        }
        if (chargeType == ChargeType.BETWEEN_TEN_AND_FIFTY) {
            return DEFAULT_CHARGE + (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
        }
        return DEFAULT_CHARGE + (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
