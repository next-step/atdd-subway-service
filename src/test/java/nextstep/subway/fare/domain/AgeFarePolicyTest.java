package nextstep.subway.fare.domain;

import nextstep.subway.common.exception.ErrorEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class AgeFarePolicyTest {

    @ParameterizedTest
    @CsvSource(value = {"6:CHILD", "13:TEENAGER", "30:ADULT"}, delimiter = ':')
    void 나이에_해당하는_정책을_찾는다(int input, AgeFarePolicy expected) {
        AgeFarePolicy result = AgeFarePolicy.findByAge(input);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 5})
    void 나이에_해당하는_정책_없는경우_예외_발생(int input) {
        Assertions.assertThatThrownBy(() -> AgeFarePolicy.findByAge(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith(ErrorEnum.NOT_EXIST_AGE_FARE_POLICY.message());
    }

    @ParameterizedTest
    @CsvSource(value = {"6:850", "13:1360"}, delimiter = ':')
    void 연령별_요금_할인_적용(int input, int expected) {
        AgeFarePolicy policy = AgeFarePolicy.findByAge(input);

        int result = policy.discount(2050);

        Assertions.assertThat(result).isEqualTo(expected);
    }

}
