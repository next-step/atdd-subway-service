package nextstep.subway.auth.domain;

import nextstep.subway.policy.DiscountPolicy;

public interface ServiceMember {
    Long getId();
    String getEmail();
    Integer getAge();
    DiscountPolicy getDiscountPolicy();
}
