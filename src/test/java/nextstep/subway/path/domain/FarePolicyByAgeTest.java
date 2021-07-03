package nextstep.subway.path.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FarePolicyByAgeTest {
    @ParameterizedTest
    @CsvSource(value = {"19:1250", "13:720", "6:450"}, delimiter = ':')
    void calculate(int age, double expectedFare) {
        FarePolicyByAge farePolicyByAge = new FarePolicyByAge(age);
        double actual = farePolicyByAge.calculate(1250);

        assertThat(actual).isEqualTo(expectedFare);
    }
}