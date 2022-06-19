package nextstep.subway.path.domain;

import nextstep.subway.path.dto.Fare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class Within10kmFareCalculatorTest {
    private FareCalculator fareCalculator;

    @BeforeEach
    void setUp() {
        this.fareCalculator = new Within10kmFareCalculator();
    }

    @ParameterizedTest
    @ValueSource(longs = { 1, 2, 3, 9 })
    @DisplayName("10km 미만 거리는 요금 계산할 수 있다.")
    void canCalculate_true(long distance) {
        assertThat(fareCalculator.canCalculate(distance)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(longs = { 10, 51, 100 })
    @DisplayName("10km 이상 거리는 요금 계산할 수 없다.")
    void canCalculate_false(long distance) {
        assertThat(fareCalculator.canCalculate(distance)).isFalse();
    }

    @ParameterizedTest
    @CsvSource(value = { "1:1250", "9:1250" }, delimiterString = ":")
    @DisplayName("요금을_계산할_수_있다")
    void calculate(long distance, long won) {
        assertThat(fareCalculator.calculate(distance)).isEqualTo(new Fare(won));
    }

}
