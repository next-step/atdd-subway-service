package nextstep.subway.path.fare.policy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.fare.Fare;

public interface DiscountFarePolicy {
    Fare discount(LoginMember loginMember, Fare fare);
}
