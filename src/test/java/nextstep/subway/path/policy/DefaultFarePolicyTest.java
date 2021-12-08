package nextstep.subway.path.policy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultFarePolicyTest {
    @DisplayName("추가요금을 계산한다")
    @ParameterizedTest
    @CsvSource(value = {"9:1250", "10:1250", "11:1350", "15:1350", "16:1450", "50:2050", "58:2150", "66:2250", "67:2350"}, delimiter = ':')
    void testCalculateOverFare(int distance, int expectedFare) {
        // given
        int extraFare = 200;
        // when
        int fare = new DefaultFarePolicy().calculateFare(extraFare, distance);
        // then
        assertThat(fare).isEqualTo(expectedFare + extraFare);
    }
}
