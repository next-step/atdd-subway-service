package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.wrapped.Money;

public interface AgeDiscountPolicy {
    Money calcFare(LoginMember member, Money money);

    boolean isSupport(LoginMember member);
}
