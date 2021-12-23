package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class DistanceFareCalculatorTest {

    @DisplayName("요금 계산 확인 - 거리별")
    @ParameterizedTest
    @CsvSource(value = { "1:1250", "10:1250", "11:1350", "15:1350", "16:1450", "50:2050", "51:2150", "58:2150", "59:2250", "67:2350" }, delimiter = ':')
    void 거리별_요금_계산_확인(int distance, int expected) {
        // when
        int 요금 = DistanceFareCalculator.calculator(distance);

        // then
        assertThat(요금).isEqualTo(expected);
    }
    
}
