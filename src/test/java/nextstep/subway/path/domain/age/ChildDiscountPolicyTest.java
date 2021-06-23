package nextstep.subway.path.domain.age;

import nextstep.subway.auth.domain.LoginMember;
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
}