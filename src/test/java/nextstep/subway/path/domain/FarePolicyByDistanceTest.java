package nextstep.subway.path.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FarePolicyByDistanceTest {
    @ParameterizedTest
    @CsvSource(value = {"8:1250", "12:1350", "50:2050", "51:2150", "59:2250"}, delimiter = ':')
    void calculate(double distance, double expectedFare) {
        FarePolicyByDistance farePolicyByDistance = new FarePolicyByDistance(distance);
        double actualFare = farePolicyByDistance.calculate(1250);

        assertThat(actualFare).isEqualTo(expectedFare);
    }
}