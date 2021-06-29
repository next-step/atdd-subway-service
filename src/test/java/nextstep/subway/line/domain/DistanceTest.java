package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간의 거리 테스트")
public class DistanceTest {

    @DisplayName("음수인 경우 예외")
    @Test
    void negativeNumber() {
        assertThatThrownBy(() -> Distance.valueOf(-1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("더하기")
    @Test
    void plus() {
        assertThat(Distance.valueOf(1).plus(2)).isEqualTo(Distance.valueOf(3));
    }

    @DisplayName("빼기")
    @Test
    void minus() {
        assertThat(Distance.valueOf(3).minus(2)).isEqualTo(Distance.valueOf(1));
    }

}
