package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExtraFareTest {
    @DisplayName("추가요금이 0보다 작으면 예외가 발생한다")
    @Test
    void valueException() {
        // when & then
        assertThatThrownBy(() -> new ExtraFare(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("추가요금은 0 보다 작을 수 없습니다.");
    }
}
