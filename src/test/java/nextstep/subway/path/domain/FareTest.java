package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class FareTest {

    @ParameterizedTest
    @DisplayName("구간에 대해 가격을 계산한다.")
    @MethodSource("priceResultSet")
    void calculatorTest(Fare fare, int result) {
        assertThat(fare.getPrice()).isEqualTo(result);
    }

    private static Stream<Arguments> priceResultSet() {
        return Stream.of(
            Arguments.of(Fare.of(13), 1250),
            Arguments.of(Fare.of(20), 1450),
            Arguments.of(Fare.of(60), 2150));
    }
}
