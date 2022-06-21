package nextstep.subway.path.domain;

import nextstep.subway.path.dto.Fare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class Over50kmFareCalculatorTest {
    private FareCalculator fareCalculator;

    @BeforeEach
    void setUp() {
        this.fareCalculator = new Over50kmFareCalculator();
    }

    @ParameterizedTest
    @ValueSource(longs = { Long.MAX_VALUE, 51 })
    @DisplayName("50km 초과 거리는 요금 계산할 수 있다.")
    void canCalculate_true(long distance) {
        assertThat(fareCalculator.canCalculate(distance)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(longs = { 1, 9, 50 })
    @DisplayName("50km 이하 거리는 요금 계산할 수 없다.")
    void canCalculate_false(long distance) {
        assertThat(fareCalculator.canCalculate(distance)).isFalse();
    }

    @ParameterizedTest
    @CsvSource(value = { "51:2150", "58:2150", "59:2250", "66:2250", "67:2350", "100:2750" }, delimiterString = ":")
    @DisplayName("요금을_계산할_수_있다")
    void calculate(long distance, long won) {
        assertThat(fareCalculator.calculate(distance)).isEqualTo(new Fare(won));
    }

}
