package nextstep.subway.fare.domain;

import nextstep.subway.exception.BadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.subway.utils.Message.AGE_FARE_POLICY_NOT_EXIST;

class AgeFarePolicyTest {

    @DisplayName("나이에 맞는 정책을 구한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @CsvSource(value = {"6:CHILD", "13:TEENAGER"}, delimiter = ':')
    void findAgeFarePolicy(int age, AgeFarePolicy expected) {
        AgeFarePolicy result = AgeFarePolicy.findByAge(age);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @DisplayName("나이에 맞는 정책이 없으면 예외를 발생한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(ints = {0, 5})
    void exception(int age) {
        Assertions.assertThatThrownBy(() -> AgeFarePolicy.findByAge(age))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(AGE_FARE_POLICY_NOT_EXIST);
    }

    @DisplayName("청소년이라면 요금에서 350원을 공제 후 20% 할인된 금액이 발생하고, 어린이라면 요금에서 350원을 공제 후 50% 할인된 금액이 발생한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @CsvSource(value = {"6:850", "13:1360"}, delimiter = ':')
    void discount(int age, int expected) {
        AgeFarePolicy policy = AgeFarePolicy.findByAge(age);

        int result = policy.discount(2050);

        Assertions.assertThat(result).isEqualTo(expected);
    }
}
