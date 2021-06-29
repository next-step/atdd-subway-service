package nextstep.subway.path.domain.age;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.AgeDiscountPolicy;
import nextstep.subway.wrapped.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nextstep.subway.path.domain.age.AgePolicyFixture.CHILD_AGE;
import static org.assertj.core.api.Assertions.assertThat;

class ChildDiscountPolicyTest {
    @ParameterizedTest
    @CsvSource(value = {"5, false", "6, true", "7, true", "8, true", "9, true", "10, true", "11, true", "12, true", "13, false"})
    @DisplayName("지원 여부를 확인한다")
    void 지원_여부를_확인한다(int age, boolean isSupport) {
        // given
        AgeDiscountPolicy discountPolicy = new ChildDiscountPolicy();
        LoginMember loginMember = new LoginMember(null, null, age);

        // when
        boolean support = discountPolicy.isSupport(loginMember);

        // then
        assertThat(support)
                .isEqualTo(isSupport);
    }

    @ParameterizedTest
    @CsvSource(value = {"1250, 450", "1550, 600", "2300, 975"})
    @DisplayName("운임을 계산한다")
    void 운임을_계산한다(int money, int except) {
        // given
        AgeDiscountPolicy discountPolicy = new ChildDiscountPolicy();
        LoginMember loginMember = new LoginMember(null, null, CHILD_AGE);
        Money defaultMoney = new Money(money);

        // when
        Money result = discountPolicy.calcFare(loginMember, defaultMoney);

        // then
        assertThat(result)
                .isEqualTo(new Money(except));
    }
}