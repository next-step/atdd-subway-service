package nextstep.subway.path.domain;

import nextstep.subway.path.dto.Fare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class Between10kmAnd50kmFareCalculatorTest {
    private FareCalculator fareCalculator;

    @BeforeEach
    void setUp() {
        this.fareCalculator = new Between10kmAnd50kmFareCalculator();
    }

    @ParameterizedTest
    @ValueSource(longs = { 10, 11, 12, 30, 49, 50 })
    @DisplayName("10km 이상 50km 이하 거리는 요금 계산할 수 있다.")
    void canCalculate_true(long distance) {
        assertThat(fareCalculator.canCalculate(distance)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(longs = { 1, 9, 51, 100 })
    @DisplayName("10km 미만 50km 초과 거리는 요금 계산할 수 없다.")
    void canCalculate_false(long distance) {
        assertThat(fareCalculator.canCalculate(distance)).isFalse();
    }

    @ParameterizedTest
    @CsvSource(value = { "10:1350", "11:1350", "15:1350", "16:1450", "20:1450", "50:2050" }, delimiterString = ":")
    @DisplayName("요금을_계산할_수_있다")
    void calculate(long distance, long won) {
        assertThat(fareCalculator.calculate(distance)).isEqualTo(new Fare(won));
    }

}
