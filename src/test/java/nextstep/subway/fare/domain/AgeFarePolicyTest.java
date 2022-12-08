package nextstep.subway.fare.domain;

import nextstep.subway.exception.AgeFarePolicyValidException;
import nextstep.subway.exception.NotFoundAgeFarePolicyException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.subway.utils.Message.AGE_FARE_POLICY_NOT_EXIST;
import static nextstep.subway.utils.Message.FARE_LESS_THAN_DEDUCTION_FARE;

class AgeFarePolicyTest {

    @DisplayName("나이에 맞는 정책을 구한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @CsvSource(value = {"6:CHILD", "13:TEENAGER"}, delimiter = ':')
    void findAgeFarePolicy(int input, AgeFarePolicy expected) {
        AgeFarePolicy result = AgeFarePolicy.findByAge(input);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @DisplayName("나이에 맞는 정책이 없으면 예외를 발생한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(ints = {0, 5})
    void exception(int input) {
        Assertions.assertThatThrownBy(() -> AgeFarePolicy.findByAge(input))
                .isInstanceOf(NotFoundAgeFarePolicyException.class)
                .hasMessageStartingWith(AGE_FARE_POLICY_NOT_EXIST);
    }

    @DisplayName("연령별 요금할인을 적용한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @CsvSource(value = {"6:850", "13:1360"}, delimiter = ':')
    void discount(int input, int expected) {
        AgeFarePolicy policy = AgeFarePolicy.findByAge(input);

        int result = policy.discount(2050);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @DisplayName("운임요금이 공제금액보다 작으면 예외를 발생한다.")
    @Test
    void discountException() {
        AgeFarePolicy policy = AgeFarePolicy.findByAge(10);

        Assertions.assertThatThrownBy(() -> policy.discount(0))
                .isInstanceOf(AgeFarePolicyValidException.class)
                .hasMessageStartingWith(FARE_LESS_THAN_DEDUCTION_FARE);
    }
}
