package nextstep.subway.path.domain.impl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FarePolicyByAgeTest {
    @ParameterizedTest
    @CsvSource(value = {"19:1250", "13:720", "6:450"}, delimiter = ':')
    void calculate(int age, double expectedFare) {
        double actual = FarePolicyByAge.findCategory(age).calculate(1250);

        assertThat(actual).isEqualTo(expectedFare);
    }
}