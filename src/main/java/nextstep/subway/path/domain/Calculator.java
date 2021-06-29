package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.wrapped.Money;

public interface Calculator {
    Money calc(Money money, LoginMember loginMember, ShortestDistance shortestDistance);
}
