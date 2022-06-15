package nextstep.subway.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.ExceptionType;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.domain.age.AgeFareType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("연령별 운임요금에 대한 단위 테스트")
class AgeFarePolicyTest {

    @DisplayName("연령이 노인이라면 운임요금은 0원이어야 한다")
    @ParameterizedTest
    @ValueSource(ints = {65, 70, 80, 90})
    void old_age_test(int param) {
        Fare totalFare = AgeFareType.calculateTotalFare(param, new Fare(0));

        assertThat(totalFare.getValue()).isEqualTo(0);
    }

    @DisplayName("연령이 성인이라면 운임요금은 기본요금인 1250원이어야 한다")
    @ParameterizedTest
    @ValueSource(ints = {19, 39, 22, 45})
    void adult_age_test(int param) {
        Fare totalFare = AgeFareType.calculateTotalFare(param, new Fare(0));

        assertThat(totalFare.getValue()).isEqualTo(1250);
    }

    @DisplayName("연령이 청소년이라면 운임요금은 기본요금에서 할인받은 720원이어야 한다")
    @ParameterizedTest
    @ValueSource(ints = {13, 15, 17, 18})
    void teenager_age_test(int param) {
        Fare totalFare = AgeFareType.calculateTotalFare(param, new Fare(0));

        assertThat(totalFare.getValue()).isEqualTo(720);
    }

    @DisplayName("연령이 어린이라면 운임요금은 기본요금에서 할인받은 450원이어야 한다")
    @ParameterizedTest
    @ValueSource(ints = {6, 7, 10, 12})
    void child_age_test(int param) {
        Fare totalFare = AgeFareType.calculateTotalFare(param, new Fare(0));

        assertThat(totalFare.getValue()).isEqualTo(450);
    }

    @DisplayName("연령이 유아라면 운임요금은 0원이어야 한다")
    @ParameterizedTest
    @ValueSource(ints = {1, 3, 4, 5})
    void toddler_age_test(int param) {
        Fare totalFare = AgeFareType.calculateTotalFare(param, new Fare(0));

        assertThat(totalFare.getValue()).isEqualTo(0);
    }

    @DisplayName("잘못된 나이를 전달하면 예외가 발생해야 한다")
    @ParameterizedTest
    @ValueSource(ints = {0, -3, -500})
    void age_exception_test(int param) {
        assertThatThrownBy(() -> {
            AgeFareType.calculateTotalFare(param, new Fare(0));
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.INVALID_AGE.getMessage());
    }
}
