package nextstep.subway.fare.policy.customer;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.member.domain.Member;

public interface CustomerPolicy {
    Fare apply(Fare fare);
    boolean isAvailable(Member member);
}
