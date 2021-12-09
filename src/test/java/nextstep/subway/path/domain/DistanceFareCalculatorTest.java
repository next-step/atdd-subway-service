package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("거리에 의한 요금 계산")
class DistanceFareCalculatorTest {

    @ParameterizedTest(name = "10㎞ 이내는 기본 요금을 반환한다. [{0}]")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void calculateOverFareShort(int distance) {
        // when
        final int fare = DistanceFareCalculator.calculateOverFare(Distance.of(distance));
        // then
        assertThat(fare).isEqualTo(DistanceFareCalculator.BASIC_FARE);
    }


    @ParameterizedTest(name = "10km 초과 ∼ 50km 까지 구간은 5km 마다 100원씩 기본 요금에서 추가된다. {0} 거리는 {1} 요금")
    @CsvSource({"11,1350", "12,1350", "17,1450", "22,1550", "32,1750", "44,1950", "49,2050", "50,2050"})
    void calculateOverFareMedium(int distance, int expectedFare) {
        // when
        final int fare = DistanceFareCalculator.calculateOverFare(Distance.of(distance));
        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    @ParameterizedTest(name = "50km 초과시 8km 마다 100원씩 추가된다. {0} 거리는 {1} 요금")
    @CsvSource({"51,2150", "52,2150", "56,2150", "107,2850", "150,3350", "170,3550", "176,3650", "178,3650"})
    void calculateOverFareLarge(int distance, int expectedFare) {
        // when
        final int fare = DistanceFareCalculator.calculateOverFare(Distance.of(distance));
        // then
        assertThat(fare).isEqualTo(expectedFare);
    }
}