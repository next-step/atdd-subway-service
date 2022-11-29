package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DistanceTest {

    @Test
    void 동등성() {
        assertThat(Distance.valueOf(5)).isEqualTo(Distance.valueOf(5));
    }

    @Test
    void sum() {
        assertThat(Distance.valueOf(5).sum(Distance.valueOf(5))).isEqualTo(Distance.valueOf(10));
    }

    @Test
    void sub() {
        assertThat(Distance.valueOf(10).sub(Distance.valueOf(5))).isEqualTo(Distance.valueOf(5));
    }

    @Test
    void sub_VALIDATE_SUB_MESSAGE() {
        assertThatThrownBy(() -> Distance.valueOf(5).sub(Distance.valueOf(5)))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }
}
