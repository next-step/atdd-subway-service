package nextstep.subway.path.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareTest {

    @DisplayName("거리에 따른 요금을 계산할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"0:1250", "10:1250", "15:1350", "20:1450", "40:1850", "50:2050", "58:2150", "122:2950"},
            delimiter = ':')
    void distance_fare(int distance, int expect) {
        Fare actual = Fare.from(distance);

        Assertions.assertThat(actual.getFare()).isEqualTo(expect);
    }
}
