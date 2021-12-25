package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AgeFareCalculatorTest {

    @DisplayName("일반인 요금")
    @Test
    void normal() {
        LoginMember loginMember = new LoginMember(1L, "abc@gmail.com", 20);
        Fare fare = AgeFareCalculator.calculateByAge(loginMember, new Fare(1250));
        assertThat(fare).isEqualTo(new Fare(1250));
    }

    @DisplayName("청소년 요금")
    @Test
    void teenager() {
        LoginMember loginMember = new LoginMember(1L, "abc@gmail.com", 15);
        Fare fare = AgeFareCalculator.calculateByAge(loginMember, new Fare(1250));
        assertThat(fare).isEqualTo(new Fare(720));
    }

    @DisplayName("어린이 요금")
    @Test
    void kid() {
        LoginMember loginMember = new LoginMember(1L, "abc@gmail.com", 7);
        Fare fare = AgeFareCalculator.calculateByAge(loginMember, new Fare(1250));
        assertThat(fare).isEqualTo(new Fare(450));
    }

}
