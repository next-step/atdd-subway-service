package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class AgeFarePolicyTest {
    @DisplayName("청소년 기본운임료를 계산한다.")
    @Test
    void calculateChildDiscountFare() {
        final LoginMember loginMember = new LoginMember(1L, "test@gmail.com", 13);
        final Fare fare = Fare.from(new BigDecimal("1250"));
        final Fare actual = AgeFarePolicy.of(loginMember, fare);
        assertThat(actual).isEqualTo(Fare.from(new BigDecimal("720")));
    }

    @DisplayName("어린이 기본운임료를 계산한다.")
    @Test
    void calculateYouthDiscountFare() {
        final LoginMember loginMember = new LoginMember(1L, "test@gmail.com", 6);
        final Fare fare = Fare.from(new BigDecimal("1250"));
        final Fare actual = AgeFarePolicy.of(loginMember, fare);
        assertThat(actual).isEqualTo(Fare.from(new BigDecimal("450")));
    }

    @DisplayName("성인 기본운임료를 계산한다.")
    @Test
    void calculateAdultFare() {
        final LoginMember loginMember = new LoginMember(1L, "test@gmail.com", 20);
        final Fare fare = Fare.from(new BigDecimal("1250"));
        final Fare actual = AgeFarePolicy.of(loginMember, fare);
        assertThat(actual).isEqualTo(Fare.from(new BigDecimal("1250")));
    }
}
