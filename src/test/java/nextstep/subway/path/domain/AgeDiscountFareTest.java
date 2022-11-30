package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeDiscountFareTest {
    @DisplayName("연령대별 할인 요금을 계산 할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"3:2450:0", "10:2450:1050", "18:2450:1680", "26:2450:2450"}, delimiter = ':')
    void calculate(int age, int fare, int expectedFare) {
        Fare expected = new Fare(expectedFare);

        Fare actual = AgeDiscountFare.calculate(age, fare);

        assertThat(actual).isEqualTo(expected);
    }
}
