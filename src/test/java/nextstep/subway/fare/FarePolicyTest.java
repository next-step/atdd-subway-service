package nextstep.subway.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("요금 정책")
class FarePolicyTest {

    private final int BASIC_FARE = 1250;
    private final int OVER_FARE = 100;

    @DisplayName("10km 이내면 기본운임만 낸다.")
    @ParameterizedTest(name = "거리가 {0} 일 때 기본운임만 낸다")
    @ValueSource(ints = {0, 1, 5, 10})
    void 요금_정책_10km_이내면_기본료(int distance) {
        assertThat(FarePolicy.fareByDistance(distance)).isEqualTo(BASIC_FARE);
    }

    @DisplayName("10km 초과 50km 이하면 5km당 100원의 추가요금을 낸다.")
    @ParameterizedTest(name = "거리가 {0} 일 때 요금은 {1}")
    @CsvSource({
        "11, 1350",
        "15, 1350",
        "16, 1450",
        "50, 2050"
    })
    void 요금_정책_10km_초과_50km_이내면_5km_마다_추가요금(int distance, int fare) {
        assertThat(FarePolicy.fareByDistance(distance)).isEqualTo(fare);
    }

    @DisplayName("50km 초과면 8km당 100원, 10km 초과 50km 이하는 5km당 100원의 추가 요금")
    @ParameterizedTest(name = "거리가 {0} 일 때 요금은 {1}")
    @CsvSource({
        "51, 2150",
        "58, 2150",
        "59, 2250",
        "66, 2250",
        "100, 2750"
    })
    void 요금_정책_50km_초과면_8km_마다_추가요금(int distance, int fare) {
        assertThat(FarePolicy.fareByDistance(distance)).isEqualTo(fare);
    }


}