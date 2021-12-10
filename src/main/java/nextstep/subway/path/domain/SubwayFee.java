package nextstep.subway.path.domain;

import org.springframework.stereotype.Component;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.discount.AdolescentDiscountPolicy;
import nextstep.subway.path.domain.discount.KidDiscountPolicy;
import nextstep.subway.path.dto.SubwayFeeRequest;

@Component
public class SubwayFee {
    private static final int ZERO_DISTANCE = 0;
    private static final int BASIC_DISTANCE = 10;
    private static final int LIMIT_DISTANCE = 50;

    private static final int LIMIT_FEE_DISTANCE = 5;
    private static final int UN_LIMIT_FEE_DISTANCE = 8;

    private static final int FREE_FEE = 0;
    private static final int BASIC_FEE = 1_250;
    private static final int OVER_FEE = 100;
    private static final int LIMIT_OVER_MAX_QUOTIENT = (LIMIT_DISTANCE - BASIC_DISTANCE) / LIMIT_FEE_DISTANCE;
    private static final int LIMIT_OVER_MAX_FEE = BASIC_FEE + LIMIT_OVER_MAX_QUOTIENT * OVER_FEE;

    public int getSubwayUsageFee(SubwayFeeRequest subwayFeeRequest) {
        int fee = calculateSubwayFee(subwayFeeRequest.getDistance(), subwayFeeRequest.getMaxLineSurcharge());
        if (subwayFeeRequest.isGuest() || subwayFeeRequest.getMemberAgeType().isNone()) {
            return fee;
        }
        return discountFeeByPolicy(fee, subwayFeeRequest.getMemberAgeType());
    }

    private int discountFeeByPolicy(int fee, LoginMember.AgeType memberAgeType) {
        if (memberAgeType.isKid()) {
            return new KidDiscountPolicy().discount(fee);
        }
        return new AdolescentDiscountPolicy().discount(fee);
    }

    private int calculateSubwayFee(int distance, int maxLineSurcharge) {
        return calculateSubwayDistanceFee(distance) + maxLineSurcharge;
    }

    private int calculateSubwayDistanceFee(int distance) {
        if (distance <= ZERO_DISTANCE) {
            return FREE_FEE;
        }

        if (distance <= BASIC_DISTANCE) {
            return BASIC_FEE;
        }

        if (distance <= LIMIT_DISTANCE) {
            return BASIC_FEE + (distance - BASIC_DISTANCE) / LIMIT_FEE_DISTANCE * OVER_FEE;
        }

        return LIMIT_OVER_MAX_FEE + (distance - LIMIT_DISTANCE) / UN_LIMIT_FEE_DISTANCE * OVER_FEE;
    }
}
