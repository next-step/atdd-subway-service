package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("구간 거리 클래스 테스트")
class DistanceTest {

    @DisplayName("생성 성공")
    @Test
    void create_distance_success() {
        assertThatNoException().isThrownBy(() -> new Distance(1));
    }

    @DisplayName("구간 거리 뺄셈 테스트")
    @Test
    void minus_distance_success() {
        int source = 10;
        int target = 6;
        int expect = 4;
        assertThat(new Distance(source).minus(new Distance(target)))
                .isEqualTo(new Distance(expect));
    }

    @DisplayName("구간 거리 덧셈 테스트")
    @Test
    void plus_distance_success() {
        int source = 10;
        int target = 6;
        int expect = 16;
        assertThat(new Distance(source).plus(new Distance(target)))
                .isEqualTo(new Distance(expect));
    }
}
