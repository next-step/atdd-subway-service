package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

public class FareCalculator {
    public static Integer calculateFare(Integer lineExtraFare, Integer distance, LoginMember member) {
        int defaultFare = calculateFareByDistance(distance) + lineExtraFare;

        AgeFarePolicy policy = AgeFarePolicy.getPolicy(member);
        return policy.discountFare(defaultFare);
    }

    private static Integer calculateFareByDistance(int distance) {
        DistanceFarePolicy policy = DistanceFarePolicy.getPolicy(distance);
        return policy.calculateFare(distance);
    }
}
