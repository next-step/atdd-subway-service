package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFarePolicyTest {
    @DisplayName("각 거리의 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "10:1250",
            "14:1250",
            "15:1350",
            "20:1450",
            "25:1550",
            "30:1650",
            "35:1750",
            "40:1850",
            "45:1950",
            "49:1950",
            "50:2050",
            "58:2150",
            "63:2150",
            "66:2250"}, delimiter = ':')
    void calculateFiveIntervalExtraFare (final int weight, final String totalFare) {
        final Path path = new Path(Distance.of(weight));
        final Fare expected = DistanceFarePolicy.from(path).plus(Fare.from(new BigDecimal("1250")));
        assertThat(expected).isEqualTo(Fare.from(new BigDecimal(totalFare)));
    }
}
