package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class ChargesTest {
    @DisplayName("요금 합 계산")
    @ParameterizedTest
    @CsvSource(value = {"1,2,3", "1000, 1050, 2050"})
    void sum(int a, int b, int sum) {
        Charge actual = Charges.of(new Charge(a), new Charge(b)).sum();
        Charge expected = new Charge(sum);
        assertThat(actual).isEqualTo(expected);
    }
}
