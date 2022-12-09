package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.common.exception.ErrorEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * 기본운임(10㎞ 이내) : 기본운임 1,250원
 * 이용 거리 초과시 추가운임 부과
 *   10km초과 ∼ 50km까지(5km마다 100원)
 *   50km초과 (8km마다 100원)
 */
public class SubwayFareCalculatorTest {

    @ParameterizedTest()
    @ValueSource(ints = {1, 5, 10})
    void 거리_10km이하_입력시_기본_운임요금_반환(int distance) {
        int fare = FareCalculator.distanceCalculate(distance);

        assertThat(fare).isEqualTo(FareCalculator.BASIC_FARE);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 거리_0km이하_유효하지않는_경우_예외_발생(int distance) {
        assertThatThrownBy(() -> FareCalculator.distanceCalculate(distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith(ErrorEnum.NOT_EXISTS_DISTANCE_AND_FARE_POLICY.message());
    }

    @ParameterizedTest()
    @CsvSource(value = {"11:1350", "15:1350","16:1450","20:1450","60:2250"}, delimiter = ':')
    void 거리_10km_이상부터_50km이하_입력시_추가_운임요금_부과(int distance, int expected) {
        int fare = FareCalculator.distanceCalculate(distance);

        assertThat(fare).isEqualTo(expected);
    }

    @ParameterizedTest()
    @CsvSource(value = {"61:2250", "68:2350", "69:2350"}, delimiter = ':')
    void 거리_50km_초과_입력시_추가_운임요금_부과(int distance, int expected) {
        int fare = FareCalculator.distanceCalculate(distance);

        assertThat(fare).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"6:500", "13:800", "19:1350"}, delimiter = ':')
    void 연령별_요금정책을_적용해서_운임요금_부과(int age, int expected) {
        int fare = FareCalculator.distanceWithAgeCalculate(15, age);

        assertThat(fare).isEqualTo(expected);
    }
}
