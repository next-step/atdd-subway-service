package nextstep.subway.line.domain;

import nextstep.subway.line.exception.DistanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DistanceUnitTest {

    private Distance distance_10;

    @BeforeEach
    void init() {
        distance_10 = new Distance(10);
    }

    @Test
    @DisplayName("길이 10에서 5를 빼면 5가 된다.")
    void minus() {
        distance_10.minus(5);
        assertThat(distance_10.getDistance()).isEqualTo(5);
    }

    @Test
    @DisplayName("길이 10에서 10보다 같거나 큰 수를 빼면 뺄 수 없다.")
    void minus_exception() {
        assertThatThrownBy(() -> {
            distance_10.minus(10);
        }).isInstanceOf(DistanceException.class)
        .hasMessageContaining(DistanceException.DISTANCE_LENGTH_MINUS_MSG);
    }
}
