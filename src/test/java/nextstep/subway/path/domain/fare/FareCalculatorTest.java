package nextstep.subway.path.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    @DisplayName("거리에 따라 추가요금이 부과된다.")
    @ParameterizedTest
    @CsvSource(value = {"1:1250", "10:1250", "11:1350", "49:2050", "50:2050", "51:2150"}, delimiter = ':')
    void calculateFareByDistance(int distance, int expected) {
        int result = FareCalculator.calculateFare(distance, new ArrayList<>());
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("노선별 추가요금 중 가장 높은 금액이 부과된다.")
    @Test
    void calculateFareByLine() {
        int result = FareCalculator.calculateFare(0, Arrays.asList(400, 200, 300, 500));
        assertThat(result).isEqualTo(1250 + 500);
    }
}
