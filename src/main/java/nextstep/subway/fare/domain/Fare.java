package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;

public class Fare {
    public static int getFare(Distance distance, LoginMember loginMember) {
        return (int) (FareByDistance.getFare(distance) * (1 - DiscountRateByAge.getDiscountRate(loginMember)));
    }
}
