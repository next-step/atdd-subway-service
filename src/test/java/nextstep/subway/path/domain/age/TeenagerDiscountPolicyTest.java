package nextstep.subway.path.domain.age;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.AgeDiscountPolicy;
import nextstep.subway.wrapped.Money;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nextstep.subway.path.domain.age.AgePolicyFixture.TEENAGER_AGE;
import static org.assertj.core.api.Assertions.assertThat;

class TeenagerDiscountPolicyTest {
    @ParameterizedTest
    @CsvSource(value = {"12, false", "13, true", "14, true", "15, true", "16, true", "17, true", "18, true", "19, false"})
    void isSupport(int age, boolean isSupport) {
        // given
        AgeDiscountPolicy discountPolicy = new TeenagerDiscountPolicy();
        LoginMember loginMember = new LoginMember(null, null, age);

        // when
        boolean support = discountPolicy.isSupport(loginMember);

        // then
        assertThat(support)
                .isEqualTo(isSupport);
    }

    @ParameterizedTest
    @CsvSource(value = {"1250, 720", "1550, 960", "2300, 1560"})
    void calcFare(int money, int except) {
        // given
        AgeDiscountPolicy discountPolicy = new TeenagerDiscountPolicy();
        LoginMember loginMember = new LoginMember(null, null, TEENAGER_AGE);
        Money defaultMoney = new Money(money);

        // when
        Money result = discountPolicy.calcFare(loginMember, defaultMoney);

        // then
        assertThat(result)
                .isEqualTo(new Money(except));
    }
}