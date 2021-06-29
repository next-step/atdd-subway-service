package nextstep.subway.path.domain.policy.distance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class OverTenAndUnderFiftyPolicyTest {

    @DisplayName("거리 10킬로 초과 50킬로까지 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"11:1350", "16:1450", "26:1650"},  delimiter = ':')
    void calculate(int distance, int expectedFare) {
        // given
        int basicFare = 1250;
        OverTenAndUnderFiftyPolicy policy = new OverTenAndUnderFiftyPolicy(distance);

        // when
        int fare = policy.calculate(basicFare);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }
}