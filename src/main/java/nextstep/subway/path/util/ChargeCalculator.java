package nextstep.subway.path.util;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.SectionWeightedEdge;
import nextstep.subway.path.types.ChargeType;

public class ChargeCalculator {
    public static int calculateTotalCharge(final int distance, final int surcharge, final LoginMember loginMember) {
        final int totalCharge = calculateDistanceCharge(distance) + surcharge;
        if (loginMember.isEmpty()) {
            return totalCharge;
        }
        return calculateAgeDiscount(totalCharge, loginMember.getAge());
    }

    private static int calculateAgeDiscount(final int totalCharge, final Integer age) {
        return SalePolicy.calculateAgeDiscountFrom(totalCharge, age);
    }

    private static int calculateYouthCharge(final int totalCharge, final int deductionPrice) {
        return totalCharge - (int) Math.round(deductionPrice * SalePolicy.YOUTH.getDiscountRate());
    }

    private static int calculateChildCharge(final int totalCharge, final int deductionPrice) {
        return totalCharge - (int) Math.round(deductionPrice * SalePolicy.CHILD.getDiscountRate());
    }

    private static int calculateDistanceCharge(final int distance) {
        return ChargeType.calculateChargeFrom(distance);
    }

    public static int calculateMaxSurcharge(final List<SectionWeightedEdge> edgeList) {
        return edgeList.stream()
                .mapToInt(sectionWeightedEdge -> sectionWeightedEdge.getSurcharge())
                .max()
                .orElse(0);
    }
}
