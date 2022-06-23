package nextstep.subway.path.util;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.types.ChargeType;

public class ChargeCalculator {
    private static int DEFAULT_CHARGE = 1_250;

    public static int calculateTotalCharge(final int distance, final int surcharge, final LoginMember loginMember) {
        final int totalCharge = calculateDistanceCharge(distance) + surcharge;
        if (loginMember.isEmpty()) {
            return totalCharge;
        }
        return calculateAgeDiscount(totalCharge, loginMember.getAge());
    }

    private static int calculateAgeDiscount(final int totalCharge, final Integer age) {
        final int deductionPrice = totalCharge - 350;
        if (13 <= age && age < 19) {
            return totalCharge - (int) Math.round(deductionPrice * 0.2);
        }
        if (6 <= age && age < 13) {
            return totalCharge - (int) Math.round(deductionPrice * 0.5);
        }
        return totalCharge;
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
