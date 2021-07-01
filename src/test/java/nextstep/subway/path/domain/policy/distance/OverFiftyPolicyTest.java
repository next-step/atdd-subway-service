package nextstep.subway.path.domain.policy.distance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class OverFiftyPolicyTest {

    @DisplayName("거리 50킬로 초과시 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"56:2150", "59:2250", "67:2350"}, delimiter = ':')
    void calculate(int distance, int expectedFare) {
        int basicFare = 1250;
        OverFiftyPolicy policy = new OverFiftyPolicy(distance);

        int calculated = policy.calculate(basicFare);

        assertThat(calculated).isEqualTo(expectedFare);
    }
}
