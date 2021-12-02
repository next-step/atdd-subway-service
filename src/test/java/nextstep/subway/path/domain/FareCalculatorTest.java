package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("이용 요금 계산기 테스트")
public class FareCalculatorTest {


    @ParameterizedTest(name = "{displayName} - [{index}] {argumentsWithNames}")
    @CsvSource(
            value = {"10:1250", "11:1350", "15:1350", "16:1450", "50:2050", "51:2150", "58:2150", "59:2250"},
            delimiter = ':'
    )
    @DisplayName("이용 요금을 계산한다.")
    void calculate(int distance, int expectedFare) {
        // given
        FareCalculator fareCalculator = new FareCalculator(distance);

        // when
        int fare = fareCalculator.calculate();

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }
}
