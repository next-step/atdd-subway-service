package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("운임")
class FareTest {

    @DisplayName("운임을 생성할 수 있다.")
    @Test
    void 운임_생성_성공() {
        assertThat(Fare.from(100)).isNotNull();
    }

    @DisplayName("운임은 0이상만 가능하다.")
    @Test
    void 운임_생성_실패() {
        assertThatThrownBy(() -> Fare.from(-1)).isInstanceOf(IllegalArgumentException.class);
    }
}
