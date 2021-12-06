package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.exception.NotValidateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceTest {

    @DisplayName("Distance 생성 시 음수인 경우 실패")
    @Test
    void valueOfFail() {
        assertThatThrownBy(() -> Distance.valueOf(-1))
            .isInstanceOf(NotValidateException.class)
            .hasMessageContaining("이상의 정수만 입력가능합니다.");
    }

    @DisplayName("길이 빼기 (10-5=5)")
    @Test
    void minus() {
        Distance minus = Distance.valueOf(10).minus(Distance.valueOf(5));
        assertThat(minus.get()).isEqualTo(5);
    }

    @DisplayName("길이 빼기 시 오른쪽 변수가 더 많은 경우 NotValidateException 실패")
    @Test
    void minusValidate() {
        assertThatThrownBy(() -> Distance.valueOf(5).minus(Distance.valueOf(10)))
            .isInstanceOf(NotValidateException.class)
            .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요.");
    }

    @DisplayName("길이 더하기 (4+5=10)")
    @Test
    void plus() {
        Distance plus = Distance.valueOf(4).plus(Distance.valueOf(5));
        assertThat(plus.get()).isEqualTo(9);
    }

    @DisplayName("길이 동등성 비교")
    @Test
    void equals() {
        assertAll(() -> {
            assertThat(Distance.valueOf(5).equals(Distance.valueOf(5))).isTrue();
            assertThat(Distance.valueOf(5).equals(Distance.valueOf(10))).isFalse();
            assertThat(Distance.valueOf(5).equals(5)).isFalse();
        });
    }
}