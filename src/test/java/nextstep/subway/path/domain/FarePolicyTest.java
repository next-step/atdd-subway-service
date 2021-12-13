package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FarePolicyTest {

    @DisplayName("추가운임 계산")
    @ParameterizedTest
    @CsvSource(value = {"5;1250", "8;1250", "22;1550", "30;1650", "50;2050", "62;2250", "132;3150"}, delimiter = ';')
    void calculateFare(int distance, int expected) {
        assertThat(FarePolicy.calculateOverFare(distance)).isEqualTo(expected);
    }

}
