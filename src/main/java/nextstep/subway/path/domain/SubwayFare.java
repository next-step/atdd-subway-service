package nextstep.subway.path.domain;

import nextstep.subway.member.domain.MemberAgeType;
import nextstep.subway.path.domain.discount.AdolescentDiscountPolicy;
import nextstep.subway.path.domain.discount.KidDiscountPolicy;
import nextstep.subway.path.dto.SubwayFareRequest;

public class SubwayFare {
    private static final int ZERO_DISTANCE = 0;
    private static final int BASIC_DISTANCE = 10;
    private static final int LIMIT_DISTANCE = 50;

    private static final int LIMIT_FARE_DISTANCE = 5;
    private static final int UN_LIMIT_FARE_DISTANCE = 8;

    private static final int FREE_FARE = 0;
    private static final int BASIC_FARE = 1_250;
    private static final int OVER_FARE = 100;
    private static final int LIMIT_OVER_MAX_QUOTIENT = (LIMIT_DISTANCE - BASIC_DISTANCE) / LIMIT_FARE_DISTANCE;
    private static final int LIMIT_OVER_MAX_FARE = BASIC_FARE + LIMIT_OVER_MAX_QUOTIENT * OVER_FARE;

    private SubwayFare() {
    }

    public static int getSubwayUsageFare(SubwayFareRequest subwayFareRequest) {
        int subwayFare = calculateSubwayFare(subwayFareRequest.getDistance(), subwayFareRequest.getLineSurcharge());
        if (subwayFareRequest.getMemberAgeType().isNone()) {
            return subwayFare;
        }
        return discountFareByPolicy(subwayFare, subwayFareRequest.getMemberAgeType());
    }

    private static int discountFareByPolicy(int fare, MemberAgeType memberAgeType) {
        if (memberAgeType.isKid()) {
            return new KidDiscountPolicy().getDiscountFare(fare);
        }
        return new AdolescentDiscountPolicy().getDiscountFare(fare);
    }

    private static int calculateSubwayFare(int distance, int lineSurcharge) {
        if (distance <= ZERO_DISTANCE) {
            return FREE_FARE;
        }
        return calculateSubwayDistanceFare(distance) + lineSurcharge;
    }

    private static int calculateSubwayDistanceFare(int distance) {
        if (distance <= BASIC_DISTANCE) {
            return BASIC_FARE;
        }

        if (distance <= LIMIT_DISTANCE) {
            return BASIC_FARE + (distance - BASIC_DISTANCE) / LIMIT_FARE_DISTANCE * OVER_FARE;
        }

        return LIMIT_OVER_MAX_FARE + (distance - LIMIT_DISTANCE) / UN_LIMIT_FARE_DISTANCE * OVER_FARE;
    }
}
