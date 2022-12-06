package nextstep.subway.path.fare.policy.discount;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.fare.Fare;

public interface DiscountFarePolicyStrategy {
    Fare discount(LoginMember loginMember, Fare fare);
}
