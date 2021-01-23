package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

public class FareCalculator {
    public static Integer calculateFare(int distance) {
        DistanceFarePolicy policy = DistanceFarePolicy.getPolicy(distance);
        return policy.calculateFare(distance);
    }

    public static Integer calculateFare(Integer lineExtraFare, Integer distance, LoginMember member) {
        int defaultFare = calculateFare(distance) + lineExtraFare;

        AgeFarePolicy policy = AgeFarePolicy.getPolicy(member);
        return policy.discountFare(defaultFare);
    }
}
