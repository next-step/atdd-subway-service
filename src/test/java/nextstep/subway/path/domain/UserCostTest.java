package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.Age;
import nextstep.subway.line.domain.Charge;
import nextstep.subway.path.domain.UserCost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class UserCostTest {

    @DisplayName("나이에 따라 UserCost 를 가진다.")
    @Test
    void findUserCostTest() {
        assertThat(UserCost.valueOf(new Age(19))).isEqualTo(UserCost.ADULT);
        assertThat(UserCost.valueOf(new Age(18))).isEqualTo(UserCost.TEENAGER);
        assertThat(UserCost.valueOf(new Age(12))).isEqualTo(UserCost.CHILD);
    }

    /**
     * Given : 경로에 대한 전체 이용 요금이 제공 되고
     * When : 사용자가 해당 요금을 결재 할때,
     * Then : 사용자별로 할인이 적용 되어진다.
     */
    @DisplayName("사용자마다 요금이 할인 된다.")
    @ParameterizedTest
    @MethodSource("provideUserCostAndExpectedCharge")
    void calculateTest(UserCost cost, long expectedCharge) {
        assertThat(cost.calculate(new Charge(1000))).isEqualTo(new Charge(expectedCharge));
    }

    private static Stream<Arguments> provideUserCostAndExpectedCharge() {
        return Stream.of(
                Arguments.of(UserCost.ADULT, 1000),
                Arguments.of(UserCost.TEENAGER, 520),
                Arguments.of(UserCost.CHILD, 325)
        );
    }
}