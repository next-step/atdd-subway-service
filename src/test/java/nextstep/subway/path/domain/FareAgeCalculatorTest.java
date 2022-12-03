package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareAgeCalculatorTest {

    @DisplayName("요금과 나이를 받아 할인 금액 계산")
    @ParameterizedTest
    @CsvSource(value = {"8:500", "15:200", "20:0"}, delimiter = ':')
    void calculate(int age, int expectedFare) {
        // when
        Fare fare = FareAgeCalculator.calculate(1350, age);

        // then
        assertThat(fare).isNotNull();
        assertThat(fare.getValue()).isEqualTo(expectedFare);
    }
}
