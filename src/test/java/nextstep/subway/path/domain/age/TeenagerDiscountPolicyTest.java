package nextstep.subway.path.domain.age;

import nextstep.subway.auth.domain.LoginMember;
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
}