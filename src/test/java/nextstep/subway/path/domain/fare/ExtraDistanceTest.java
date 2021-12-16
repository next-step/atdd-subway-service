package nextstep.subway.path.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("거리 비례 요금 정책 기능")
class ExtraDistanceTest {

    @ParameterizedTest(name = "기본요금 거리: [{0}]=0")
    @ValueSource(ints = {1, 10})
    void getOverDistanceBasic(int distance) {
        // given when
        int overDistance = ExtraDistance.getOverDistance(distance);

        // then
        assertThat(overDistance).isZero();
    }

    @ParameterizedTest(name = "10km 초과 50km 이상인 거리: [{0}]={1}")
    @CsvSource(value = {"11, 1", "16, 2", "41, 7", "46, 8"})
    void getOverDistance10km(int distance, int excepted) {
        // given when
        int overDistance = ExtraDistance.getOverDistance(distance);

        // then
        assertThat(overDistance).isEqualTo(excepted);
    }

    @ParameterizedTest(name = "50km 초과인 거리: [{0}]={1}")
    @CsvSource(value = {"51, 6", "59, 7", "67, 8", "75, 9"})
    void getOverDistance50km(int distance, int excepted) {
        // given when
        int overDistance = ExtraDistance.getOverDistance(distance);

        // then
        assertThat(overDistance).isEqualTo(excepted);
    }
}
