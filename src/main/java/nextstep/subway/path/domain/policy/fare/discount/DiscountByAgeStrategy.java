package nextstep.subway.path.domain.policy.fare.discount;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.Fare;

public interface DiscountByAgeStrategy {
    int discountBy(LoginMember member, Fare fare);
    boolean isAvailable(LoginMember member);
}
