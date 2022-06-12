package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidDistanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {

    @DisplayName("Distance 예외 생성")
    @ParameterizedTest
    @ValueSource(ints = { -10, -5, 0 })
    void distance(int value) {
        assertThatThrownBy(() -> {
            new Distance(value);
        }).isInstanceOf(InvalidDistanceException.class);
    }

    @DisplayName("거리 증가 기능")
    @Test
    void plusDistance() {
        Distance distance = new Distance(10);
        assertThat(distance.plusDistance(10)).isEqualTo(new Distance(20));
    }

    @DisplayName("거리 감소 기능")
    @Test
    void minusDistance() {
        Distance distance = new Distance(30);
        assertThat(distance.minusDistance(10)).isEqualTo(new Distance(20));
    }
}
