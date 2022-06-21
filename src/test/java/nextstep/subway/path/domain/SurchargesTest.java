package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class SurchargesTest {
    @DisplayName("추가요금 합 계산")
    @ParameterizedTest
    @CsvSource(value = {"1,2,3", "1000, 1050, 2050"})
    void sum(int a, int b, int sum) {
        Surcharge actual = Surcharges.of(new Surcharge(a), new Surcharge(b)).sum();
        Surcharge expected = new Surcharge(sum);
        assertThat(actual).isEqualTo(expected);
    }
}
