package nextstep.subway.fare.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeFarePolicyTest {
    private final AgeFarePolicy policy = new AgeFarePolicy();

    @ParameterizedTest
    @ValueSource(ints = {7, 10})
    void 어린이_요금(int age) {
        assertThat(policy.calculate(Fare.from(1600), age))
                .isEqualTo(Fare.from(975));
    }

    @ParameterizedTest
    @ValueSource(ints = {15, 17})
    void 청소년_요금(int age) {
        assertThat(policy.calculate(Fare.from(1600), age))
                .isEqualTo(Fare.from(1350));
    }

    @ParameterizedTest
    @ValueSource(ints = {20, 30})
    void 나이할인없는_요금(int age) {
        assertThat(policy.calculate(Fare.from(1600), age))
                .isEqualTo(Fare.from(1600));
    }


}
