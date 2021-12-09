package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class DistanceTest {

    @Test
    @DisplayName("거리를 생성합니다.")
    public void constructor_success() throws Exception {
        // given
        int distance = 1;

        // when
        Distance result = new Distance(distance);

        // then
        assertThat(result.getDistance()).isEqualTo(distance);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("거리가 0보다 작거나 같으면 생성에 실패한다.")
    public void constructor_fail(int distance) throws Exception {
        // when, then
        assertThatThrownBy(() -> new Distance(distance))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("입력받은 거리를 뺀 뒤 새로운 Distance 객체를 리턴한다.")
    public void minus() throws Exception {
        // given
        int distanceValue = 2;
        Distance distance = new Distance(distanceValue);

        int inputDistanceValue = 1;
        Distance inputDistance = new Distance(inputDistanceValue);

        // when
        Distance result = distance.minus(inputDistance);

        // then
        assertThat(result.getDistance()).isEqualTo(distanceValue - inputDistanceValue);
    }

    @Test
    @DisplayName("입력받은 거리를 더한 뒤 새로운 Distance 객체를 리턴한다.")
    public void plus() throws Exception {
        // given
        int distanceValue = 2;
        Distance distance = new Distance(distanceValue);

        int inputDistanceValue = 1;
        Distance inputDistance = new Distance(inputDistanceValue);

        // when
        Distance result = distance.plus(inputDistance);

        // then
        assertThat(result.getDistance()).isEqualTo(distanceValue + inputDistanceValue);
    }
}