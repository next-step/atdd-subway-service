package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.common.exception.SubwayException;

public class DistanceTest {
    @DisplayName("거리의 길이를 0 이하로 생성하면 에러가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, Integer.MIN_VALUE})
    void construct_throwErrorWhenLowerThanOne(int distance) {
        assertThatExceptionOfType(SubwayException.class)
            .isThrownBy(() -> new Distance(distance))
            .withMessage("1 이상의 길이만 입력 가능합니다.");
    }

    @DisplayName("거리 더하기")
    @Test
    void add() {
        Distance result = new Distance(10)
            .add(new Distance(5));

        assertThat(result)
            .isEqualTo(new Distance(15));
    }

    @DisplayName("거리 빼기")
    @Test
    void subtract() {
        Distance result = new Distance(10)
            .subtract(new Distance(9));

        assertThat(result)
            .isEqualTo(new Distance(1));
    }

    @DisplayName("거리 빼기 에러")
    @Test
    void subtract_error() {
        Distance distance = new Distance(10);
        Distance distance2 = new Distance(10);

        assertThatExceptionOfType(SubwayException.class)
            .isThrownBy(() -> distance.subtract(distance2))
            .withMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }
}
