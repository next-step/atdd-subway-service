package nextstep.subway.fare.domain;

import nextstep.subway.exception.FareValidException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.subway.utils.Constant.BASIC_FARE;
import static nextstep.subway.utils.Message.INVALID_OVER_DISTANCE;


/**
 * 기본운임(10㎞ 이내) : 기본운임 1250원
 * 이용 거리초과 시 추가운임 부과
 * 10km초과∼50km까지(5km마다 100원)
 * 50km초과 시 (8km마다 100원)
 */
class FareCalculatorTest {

    private FareCalculator fareCalculator;

    @BeforeEach
    void setUp() {
        fareCalculator = new FareCalculator();
    }

    @DisplayName("거리가 10km 이내일 때 기본운임은 1250원이다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(ints = {0, 5, 10})
    void basicFare(int input) {
        FareCalculator fareCalculator = new FareCalculator();

        int fare = fareCalculator.calculate(input);

        Assertions.assertThat(fare).isEqualTo(BASIC_FARE);
    }

    @DisplayName("거리가 10km 초과 50km 이하일 때 요금을 계산한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @CsvSource(value = {"11:1350", "15:1350", "23:1550", "50:2050"}, delimiter = ':')
    void tenToFiftyFare(int input, int expected) {
        int fare = fareCalculator.calculate(input);

        Assertions.assertThat(fare).isEqualTo(expected);
    }


    @DisplayName("거리가 50km 초과일 때 요금을 계산한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @CsvSource(value = {"51:2150", "74:2350", "80:2450"}, delimiter = ':')
    void overFiftyFare(int input, int expected) {
        int fare = fareCalculator.calculate(input);

        Assertions.assertThat(fare).isEqualTo(expected);
    }

    @DisplayName("거리가 0 미만일 때 예외를 발생한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(ints = {-1, -10})
    void exception(int input) {
        Assertions.assertThatThrownBy(() -> fareCalculator.calculate(input))
                .isInstanceOf(FareValidException.class)
                .hasMessageStartingWith(INVALID_OVER_DISTANCE);
    }

    @DisplayName("지하철 노선 추가요금이 있는 경우 노선요금도 더해서 계산한다.")
    @Test
    void calculateWithLineFare() {
        FareCalculator fareCalculator = FareCalculator.from(100);

        int result = fareCalculator.calculate(10);

        Assertions.assertThat(result).isEqualTo(1350);
    }


    @DisplayName("연령별 요금정책을 적용해서 운임요금을 계산한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @CsvSource(value = {"6:500", "13:800", "19:1350"}, delimiter = ':')
    void calculateWithAgeDiscount(int input, int expected) {
        FareCalculator fareCalculator = FareCalculator.of(0, input);

        int result = fareCalculator.calculate(15);

        Assertions.assertThat(result).isEqualTo(expected);
    }



}
