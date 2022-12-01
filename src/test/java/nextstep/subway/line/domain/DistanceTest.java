package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DistanceTest {

    @Test
    void 동등성() {
        assertThat(Distance.from(5)).isEqualTo(Distance.from(5));
    }

    @Test
    void sum() {
        assertThat(Distance.from(5).sum(Distance.from(5))).isEqualTo(Distance.from(10));
    }

    @Test
    void sub() {
        assertThat(Distance.from(10).sub(Distance.from(5))).isEqualTo(Distance.from(5));
    }

    @Test
    void sub_VALIDATE_SUB_MESSAGE() {
        assertThatThrownBy(() -> Distance.from(5).sub(Distance.from(5)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }
}
