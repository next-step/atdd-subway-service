package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DistanceTest {

    @DisplayName("구간의 거리가 비교값보다 작거나 같으면 성공한다")
    @Test
    void isSmallOrEqualTo() {
        Distance distance = new Distance(3);

        assertAll(
                () -> assertThat(distance.isSmallOrEqualTo(new Distance(6))).isTrue(),
                () -> assertThat(distance.isSmallOrEqualTo(new Distance(1))).isFalse()
        );
    }

    @Test
    void minus() {
        Distance distance = new Distance(3);

        distance.minus(new Distance(2));

        assertThat(distance.value()).isEqualTo(1);
    }

    @Test
    void isBiggerThen() {
        Distance distance = new Distance(3);

        assertThat(distance.isBiggerThen(1)).isTrue();
    }
}
