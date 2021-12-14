package nextstep.subway.path.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FareTest {

    @DisplayName("10km 이하인 경우 기본 요금만 부과된다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 6, 10})
    void test_10km_이하_요금(int distance) {
        assertThat(Fare.of(Distance.of(distance)).getFare()).isEqualTo(Fare.BASE_FARE);
    }

    @DisplayName("11km 이상, 50km 이하면 기본요금 + 5km당 100원의 추가 요금이 부과된다.")
    @ParameterizedTest
    @CsvSource(value = {"11:1350", "15:1350", "45:1950", "49:2050", "50:2050"}, delimiter = ':')
    void test_11km이상_50km_이하_요금(int distance, int expectedFare) {
        assertThat(Fare.of(Distance.of(distance)).getFare()).isEqualTo(expectedFare);
    }

    @DisplayName("11km 이상, 50km 이하면 기본요금 + 5km당 100원의 추가 요금이 부과된다.")
    @ParameterizedTest
    @CsvSource(value = {"51:2150", "58:2150", "170:3550", "177:3650", "178:3650"}, delimiter = ':')
    void test_51km이상_요금(int distance, int expectedFare) {
        assertThat(Fare.of(Distance.of(distance)).getFare()).isEqualTo(expectedFare);
    }
}