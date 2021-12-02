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

    @Test
    @DisplayName("어린이는 350원을 공제한 금액의 50%할인")
    void discountFare_child() {
        // when
        Fare discountFare = FareDiscountPolicy.from(anyEmailMember(6))
            .discountFare(Fare.from(2150));

        // then
        assertThat(discountFare).isEqualTo(Fare.from(1250));
    }

    @Test
    @DisplayName("청소년은 350원을 공제한 금액의 20%할인")
    void discountFare_youth() {
        // when
        Fare discountFare = FareDiscountPolicy.from(anyEmailMember(13))
            .discountFare(Fare.from(2150));

        // then
        assertThat(discountFare).isEqualTo(Fare.from(710));
    }

    @Test
    @DisplayName("성인은 할인되는 금액이 없다")
    void discountFare_guest() {
        // when
        Fare discountFare = FareDiscountPolicy.from(anyEmailMember(19))
            .discountFare(Fare.from(2150));

        // then
        assertThat(discountFare).isEqualTo(Fare.from(0));
    }

    private LoginMember anyEmailMember(int age) {
        return LoginMember.of(1, Email.from("email@email.com"), Age.from(age));
    }
}
