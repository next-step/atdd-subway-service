package nextstep.subway.line.domain;

import nextstep.subway.exception.NotValidDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.subway.exception.type.ValidExceptionType.NOT_SHORT_VALID_DISTANCE;
import static nextstep.subway.exception.type.ValidExceptionType.NOT_ZERO_VALID_DISTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class DistanceTest {
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("거리가 0보다 작거나 같으면 예외를 발생시킨다.")
    void distanceIfMinZeroException(int distance) {
        assertThatThrownBy(() -> {
            Distance.from(distance);
        }).isInstanceOf(NotValidDataException.class)
                .hasMessageContaining(NOT_ZERO_VALID_DISTANCE.getMessage());
    }

    @Test
    @DisplayName("유입받은 숫자로 부터 갖고있는 거리를 뺀다.")
    void distanceMinus() {
        Distance distance = Distance.from(10);
        Distance minusDistance = distance.minus(2);

        assertThat(minusDistance.getDistance()).isEqualTo(8);
    }

    @Test
    @DisplayName("유입받은 숫자로 부터 갖고있는 거리를 더한다.")
    void distancePlus() {
        Distance distance = Distance.from(10);
        Distance minusDistance = distance.plus(Distance.from(20));

        assertThat(minusDistance.getDistance()).isEqualTo(30);
    }

    @Test
    @DisplayName("기존 역의 사이즈보다 거리가 크면 오류를 리턴한다")
    void validCheckOverDistance() {
        Distance distance = Distance.from(10);

        boolean result = distance.isOverDistance(12);

        assertThat(result).isTrue();
    }
}
