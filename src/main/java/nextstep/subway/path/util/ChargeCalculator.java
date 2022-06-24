package nextstep.subway.path.util;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.SectionWeightedEdge;
import nextstep.subway.path.types.ChargeType;

public class ChargeCalculator {
    private static final int DEFAULT_CHARGE = 1_250;
    private static final int DEDUCTION_AMOUNT = 350;

    public static int calculateTotalCharge(final int distance, final int surcharge, final LoginMember loginMember) {
        final int totalCharge = calculateDistanceCharge(distance) + surcharge;
        if (loginMember.isEmpty()) {
            return totalCharge;
        }
        return calculateAgeDiscount(totalCharge, loginMember.getAge());
    }

    private static int calculateAgeDiscount(final int totalCharge, final Integer age) {
        final int deductionPrice = totalCharge - DEDUCTION_AMOUNT;
        final SalePolicy salePolicy = SalePolicy.getPolicyByAge(age);
        if (salePolicy.equals(SalePolicy.YOUTH)) {
            return calculateYouthCharge(totalCharge, deductionPrice);
        }
        if (salePolicy.equals(SalePolicy.CHILD)) {
            return calculateChildCharge(totalCharge, deductionPrice);
        }
        return totalCharge;
    }

    private static int calculateYouthCharge(final int totalCharge, final int deductionPrice) {
        return totalCharge - (int) Math.round(deductionPrice * SalePolicy.YOUTH.getDiscountRate());
    }

    private static int calculateChildCharge(final int totalCharge, final int deductionPrice) {
        return totalCharge - (int) Math.round(deductionPrice * SalePolicy.CHILD.getDiscountRate());
    }

    private static int calculateDistanceCharge(final int distance) {
        final ChargeType chargeType = ChargeType.of(distance);
        if (chargeType.equals(ChargeType.DEFAULT_CHARGE)) {
            return DEFAULT_CHARGE;
        }
        if (chargeType.equals(ChargeType.LEVEL_ONE_CHARGE)) {
            return DEFAULT_CHARGE + (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
        }
        return DEFAULT_CHARGE + (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

    public static int calculateMaxSurcharge(final List<SectionWeightedEdge> edgeList) {
        return edgeList.stream()
                .mapToInt(sectionWeightedEdge -> sectionWeightedEdge.getSurcharge())
                .max()
                .orElse(0);
    }
}
