package nextstep.subway.path.domain.age;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.AgeDiscountPolicy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultAgeDiscountPolicyTest {
    @ParameterizedTest
    @CsvSource(value = {"5, true", "6, false", "18, false", "19, true"})
    void isSupport(int age, boolean isSupport) {
        // given
        AgeDiscountPolicy discountPolicy = new DefaultAgeDiscountPolicy();
        LoginMember loginMember = new LoginMember(null, null, age);

        // when
        boolean support = discountPolicy.isSupport(loginMember);

        // then
        assertThat(support)
                .isEqualTo(isSupport);
    }
}