package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class PriceCalculatorTest {

    @ParameterizedTest
    @CsvSource(value = {"9:1250", "10:1250", "11:1350", "15:1350", "50:2050", "51:2150", "178:3650"}, delimiter = ':')
    @DisplayName("입력된 거리에 따른 요금을 리턴한다.")
    public void calculate(int distance, int expectResult) throws Exception {
        // when
        int result = PriceCalculator.calculate(distance);

        // then
        assertThat(result).isEqualTo(expectResult);
    }
}