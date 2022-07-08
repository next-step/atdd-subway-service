package nextstep.subway.fare;

import nextstep.subway.auth.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AgeFarePolicyTest {
    private AgeFarePolicy ageFarePolicy = new AgeFarePolicy();

    @DisplayName("비회원은 할인하지 않는다")
    @Test
    void calculate_guest() {
        Fare fare = new Fare(1000);
        LoginMember loginMember = LoginMember.guest();

        Fare result = ageFarePolicy.calculate(fare, loginMember);

        assertThat(result).isEqualTo(new Fare(1000));
    }

    @DisplayName("어린이는 50프로 할인한다")
    @Test
    void calculate_children() {
        Fare fare = new Fare(1350);
        LoginMember loginMember = new LoginMember(1L, "children", 7);

        Fare result = ageFarePolicy.calculate(fare, loginMember);

        assertThat(result).isEqualTo(new Fare(500));
    }

    @DisplayName("청소년은 50프로 할인한다")
    @Test
    void calculate_teenager() {
        Fare fare = new Fare(1350);
        LoginMember loginMember = new LoginMember(1L, "teenager", 15);

        Fare result = ageFarePolicy.calculate(fare, loginMember);

        assertThat(result).isEqualTo(new Fare(800));
    }
}
