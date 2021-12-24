package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.Path;

public class Fare {
    public static int getFare(Path path, LoginMember loginMember) {
        return (int) ((FareByDistance.getFare(path.getDistance())
                + path.getMaxOverFareOfLine())
                * (1 - DiscountRateByAge.getDiscountRate(loginMember)));
    }
}
