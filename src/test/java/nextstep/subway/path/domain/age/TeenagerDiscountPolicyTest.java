package nextstep.subway.path.domain.age;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.wrapped.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TeenagerDiscountPolicyTest {
    @ParameterizedTest
    @CsvSource(value = {"12, false", "13, true", "14, true", "15, true", "16, true", "17, true", "18, true", "19, false"})
    void isSupport(int age, boolean isSupport) {
        assertThat(new TeenagerDiscountPolicy().isSupport(new LoginMember(null, null, age)))
                .isEqualTo(isSupport);
    }

    @ParameterizedTest
    @CsvSource(value = {"1250, 720", "1550, 960", "2300, 1560"})
    void calcFare(int money, int except) {
        assertThat(new TeenagerDiscountPolicy().calcFare(new LoginMember(null, null, 13), new Money(money)))
                .isEqualTo(new Money(except));
    }
}