package nextstep.subway.path.domain;

import nextstep.subway.member.domain.Member;

public interface DiscountPolicy {
    int calculate(Member member);
}
