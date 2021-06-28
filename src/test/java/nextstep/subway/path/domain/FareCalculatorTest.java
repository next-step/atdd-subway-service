package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.calculator.FareCalculator;

public class FareCalculatorTest {

    @ParameterizedTest
    @DisplayName("구간에 대해 가격을 계산한다.")
    @MethodSource("priceResultSet")
    void calculatorTest(int distance, int result) {
        assertThat(FareCalculator.getPrice(distance, LoginMember.DEFAULT_USER, 0)).isEqualTo(result);
    }

    @Test
    @DisplayName("추가요금이 있는 구간에 대해 가격을 계산한다.")
    void calculatorAddLineTest() {
        assertThat(FareCalculator.getPrice(13, LoginMember.DEFAULT_USER, 100)).isEqualTo(1350);
    }

    @Test
    @DisplayName(" 13세 이상~19세 미만은 청소년: 운임에서 350원을 공제한 금액의 20%할인 받는다")
    void calculatorDisCountTest() {
        assertThat(FareCalculator.getPrice(60, new LoginMember(1L, "", 13), 0)).isEqualTo(1720);
        assertThat(FareCalculator.getPrice(60, new LoginMember(1L, "", 12), 0)).isEqualTo(1075);
    }

    private static Stream<Arguments> priceResultSet() {
        return Stream.of(
            Arguments.of(13, 1250),
            Arguments.of(20, 1450),
            Arguments.of(60, 2150));
    }
}
