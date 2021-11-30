package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.domain.Age;
import nextstep.subway.common.domain.Email;
import nextstep.subway.common.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("할인 요금 정책")
class FareDiscountPolicyTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> FareDiscountPolicy.from(anyEmailMember(10)));
    }

    @Test
    @DisplayName("사용자는 필수")
    void instance_nullArgument_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> FareDiscountPolicy.from(null))
            .withMessageEndingWith("필수입니다.");
    }

    @ParameterizedTest(name = "[{index}] {0} 나이일 경우 2150원의 할인 금액은 {1}")
    @DisplayName("연령대별 2150원에 대한 할인 요금 계산")
    @CsvSource({"6,1250", "12,1250", "13,710", "19,0"})
    void discountFare(int age, int expectedFare) {
        // when
        Fare discountFare = FareDiscountPolicy.from(anyEmailMember(age))
            .discountFare(Fare.from(2150));

        // then
        assertThat(discountFare).isEqualTo(Fare.from(expectedFare));
    }

    private LoginMember anyEmailMember(int age) {
        return LoginMember.of(1, Email.from("email@email.com"), Age.from(age));
    }
}
