package nextstep.subway.path.domain;

import nextstep.subway.fare.FarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금")
class FarePolicyTest {

    @DisplayName("요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"1300:1200:500:0.8:1600", "1000:1200:200:0.5:1000"}, delimiter = ':')
    void calculate_10km(int lineFare, int distanceFare, int deductionFare, double rate, int result) {
        FarePolicy farePolicy = new FarePolicy(lineFare, distanceFare, deductionFare, rate);
        assertThat(farePolicy.calculate()).isEqualTo(result);
    }
}
