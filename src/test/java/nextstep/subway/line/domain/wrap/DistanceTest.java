package nextstep.subway.line.domain.wrap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Domain:distance")
class DistanceTest {

    @ParameterizedTest
    @ValueSource(ints = {0, Integer.MIN_VALUE})
    @DisplayName("구간 거리가 올바르지 않은 경우 객체 생성 시, 예외")
    public void exception(int given) {
        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Distance(given));
    }

    @Test
    @DisplayName("현재 구간 거리와 대상 구간 거리의 차")
    public void minusDistance() {
        // Given
        int targetDistance = 10;
        int distance = 100;
        Distance actual = new Distance(distance);

        // When
        actual.minusDistance(targetDistance);

        // Then
        assertThat(actual.getDistance()).isEqualTo(distance - targetDistance);
    }

    @Test
    @DisplayName("대상 구간 거리가 현재 구간 거리보다 큰 경우 예외")
    public void throwException_WhenTargetDistanceIsOverThanDistance() {
        // Given
        int targetDistance = 101;
        int distance = 100;
        Distance actual = new Distance(distance);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> actual.minusDistance(targetDistance));
    }

    @Test
    @DisplayName("현재 구간 거리와 대상 구간 거리의 합")
    public void plusDistance() {
        int targetDistance = 10;
        int distance = 100;
        Distance actual = new Distance(distance);

        // When
        actual.plusDistance(targetDistance);

        // Then
        assertThat(actual.getDistance()).isEqualTo(distance + targetDistance);
    }
}
