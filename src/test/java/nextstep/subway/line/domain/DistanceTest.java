package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DistanceTest {

    @Test
    void 동등성() {
        assertThat(Distance.of(5)).isEqualTo(Distance.of(5));
    }

    @Test
    void sum() {
        assertThat(Distance.of(5).sum(Distance.of(5))).isEqualTo(Distance.of(10));
    }

    @Test
    void sub() {
        assertThat(Distance.of(10).sub(Distance.of(5))).isEqualTo(Distance.of(5));
    }

    @Test
    void sub_VALIDATE_SUB_MESSAGE() {
        assertThatThrownBy(() -> Distance.of(5).sub(Distance.of(5)))
            .isInstanceOf(RuntimeException.class)
            .hasMessage(Distance.VALIDATE_SUB_MESSAGE);
    }
}
