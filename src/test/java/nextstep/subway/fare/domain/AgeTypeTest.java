package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.policy.DiscountPolicy;
import nextstep.subway.fare.policy.KidsDiscountPolicy;
import nextstep.subway.fare.policy.NoneDiscountPolicy;
import nextstep.subway.fare.policy.TeenagersDiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AgeTypeTest {
    @DisplayName("일반 회원 할인 정책 조회")
    @Test
    void basic_discount_policy() {
        // given
        AuthMember authMember = new LoginMember(0L, "basic@mail.com", 19);
        // when
        DiscountPolicy discountPolicy = AgeType.getDiscountPolicy(authMember);
        // then
        assertThat(discountPolicy).isInstanceOf(NoneDiscountPolicy.class);
    }

    @DisplayName("청소년 회원 할인 정책 조회")
    @Test
    void teenagers_discount_policy() {
        // given
        AuthMember authMember = new LoginMember(0L, "teenagers@mail.com", 13);
        // when
        DiscountPolicy discountPolicy = AgeType.getDiscountPolicy(authMember);
        // then
        assertThat(discountPolicy).isInstanceOf(TeenagersDiscountPolicy.class);
    }

    @DisplayName("어린이 회원 할인 정책 조회")
    @Test
    void kids_discount_policy() {
        // given
        AuthMember authMember = new LoginMember(0L, "kids@mail.com", 6);
        // when
        DiscountPolicy discountPolicy = AgeType.getDiscountPolicy(authMember);
        // then
        assertThat(discountPolicy).isInstanceOf(KidsDiscountPolicy.class);
    }
}