package nextstep.subway.path.domain.age;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.wrapped.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ChildDiscountPolicyTest {
    @ParameterizedTest
    @CsvSource(value = {"5, false", "6, true", "7, true", "8, true", "9, true", "10, true", "11, true", "12, true", "13, false"})
    void isSupport(int age, boolean isSupport) {
        assertThat(new ChildDiscountPolicy().isSupport(new LoginMember(null, null, age)))
                .isEqualTo(isSupport);
    }

    @ParameterizedTest
    @CsvSource(value = {"1250, 450", "1550, 600", "2300, 975"})
    void calcFare(int money, int except) {
        assertThat(new ChildDiscountPolicy().calcFare(new LoginMember(null, null, 6), new Money(money)))
                .isEqualTo(new Money(except));
    }
}