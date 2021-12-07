package nextstep.subway.path.policy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultFarePolicyTest {
    @ParameterizedTest
    @CsvSource(value = {"9:1250", "10:1250", "11:1350", "15:1350", "16:1450", "50:2050", "58:2150", "66:2250", "67:2350"}, delimiter = ':')
    void test(int distance, int expectedFare) {
        // when
        int fare = new DefaultFarePolicy().calculateOverFare(distance);
        // then
        assertThat(fare).isEqualTo(expectedFare);
    }
}
