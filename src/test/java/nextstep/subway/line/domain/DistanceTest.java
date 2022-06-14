package nextstep.subway.line.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class DistanceTest {

    @Test
    void 생성시_0보다_작을_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Distance(-1));
    }

    @Test
    void 거리를_생성할_수_있다() {
        assertThat(new Distance(0).getDistance()).isZero();
    }

    @Test
    void 더할수_있다() {
        Distance distance = new Distance(2).plus(new Distance(1));

        assertThat(distance).isEqualTo(new Distance(3));
    }

    @Test
    void 뺄_수_있다() {
        Distance distance = new Distance(2).minus(new Distance(1));

        assertThat(distance).isEqualTo(new Distance(1));
    }

    @Test
    void 뺄때_결과값이_음수_또는_0이_될_수_없다() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> new Distance(1).minus(new Distance(2)));
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> new Distance(1).minus(new Distance(1)));
    }

}