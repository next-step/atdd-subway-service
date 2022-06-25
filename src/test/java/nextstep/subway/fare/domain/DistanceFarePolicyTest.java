package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFarePolicyTest {
    private static final Fare BASIC_FARE = new Fare(1_250);
    private final DistanceFarePolicy policy = new DistanceFarePolicy();

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10})
    void 기본요금_10키로이하(int distance) {
        assertThat(policy.calculate(new Distance(distance)))
                .isEqualTo(BASIC_FARE);
    }

    @ParameterizedTest
    @CsvSource(value = {"11:1350", "20:1450", "50:2050"}, delimiter = ':')
    void 요금_10키로초과_50키로이하(int distance, int fare) {
        assertThat(policy.calculate(new Distance(distance)))
                .isEqualTo(new Fare(fare));
    }

    @ParameterizedTest
    @CsvSource(value = {"55:2250", "58:2350"}, delimiter = ':')
    void 요금_50키로초과(int distance, int fare) {
        assertThat(policy.calculate(new Distance(distance)))
                .isEqualTo(new Fare(fare));
    }

}
