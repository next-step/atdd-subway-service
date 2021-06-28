package nextstep.subway.path.domain.age;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.AgeDiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultAgeDiscountPolicyTest {
    @ParameterizedTest
    @CsvSource(value = {"5, true", "6, false", "18, false", "19, true"})
    @DisplayName("지원 여부를 확인한다")
    void 지원_여부를_확인한다(int age, boolean isSupport) {
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