package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareCalculatorTest {

    @DisplayName("요금 계산 확인 - 거리별")
    @ParameterizedTest
    @CsvSource(value = { "1:1250", "10:1250", "11:1350", "15:1350", "16:1450", "50:2050", "51:2150", "58:2150", "59:2250", "67:2350" }, delimiter = ':')
    void 거리별_요금_계산_확인(int distance, int expected) {
        // when
        int 요금 = FareCalculator.calculator(distance, 0, 0);

        // then
        assertThat(요금).isEqualTo(expected);
    }
    
    @DisplayName("요금 계산 확인 - 노선별 추가요금")
    @ParameterizedTest
    @CsvSource(value = { "1:1550", "10:1550", "11:1650", "15:1650", "16:1750", "50:2350", "51:2450", "58:2450", "59:2550", "67:2650" }, delimiter = ':')
    void 노선별_추가요금_계산_확인(int distance, int expected) {
        // when
        int 요금 = FareCalculator.calculator(distance, 300, 0);

        // then
        assertThat(요금).isEqualTo(expected);
    }
    
    @DisplayName("요금 계산 확인 - 연령별 할인요금")
    @ParameterizedTest
    @CsvSource(value = { "1:7:450", "10:15:720", "11:30:1350" }, delimiter = ':')
    void 연령별_요금_계산_확인(int distance, int age, int expected) {
        // when
        int 요금 = FareCalculator.calculator(distance, 0, age);
        
        // then
        assertThat(요금).isEqualTo(expected);
    }
}
