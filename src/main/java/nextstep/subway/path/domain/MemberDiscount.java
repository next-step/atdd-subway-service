package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.Money;

public interface MemberDiscount {

    Money discount(final LoginMember loginMember, final Money fee);
}
