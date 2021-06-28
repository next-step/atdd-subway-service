package nextstep.subway.path.domain.calculator;

import nextstep.subway.auth.domain.LoginMember;

public class FareCalculator {

    private static final int BASE_FARE = 1250;

    public static int getPrice(int distance, LoginMember member, int aditionalFare) {
        int fare = BASE_FARE + aditionalFare;
        fare += DistanceCalculator.getDistanceFare(distance);
        fare = AgeCalculator.discountRate(fare, member);
        return fare;
    }

}
