package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PathFareAgeTest {

    @DisplayName("청소년 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    void calculatorFare_youth(int age) {
        final LoginMember loginMember = new LoginMember(1L, "test@gmail.com", age);
        final Fare fare = PathFareAge.of(loginMember);
        final BigDecimal expected = new BigDecimal("720");
        assertThat(fare.value().equals(expected)).isTrue();
    }

    @DisplayName("어린이 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    void calculatorFare_child(int age) {
        final LoginMember loginMember = new LoginMember(1L, "test@gmail.com", age);
        final Fare fare = PathFareAge.of(loginMember);
        final BigDecimal expected = new BigDecimal("450");
        assertThat(fare.value().equals(expected)).isTrue();
    }
}
