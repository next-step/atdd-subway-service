package nextstep.subway.line.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class DistanceTest {
    private Distance 길이_5;
    private Distance 길이_15;
    private Distance 길이_20;

    @BeforeEach
    void setup() {
        길이_5 = new Distance(5);
        길이_15 = new Distance(15);
        길이_20 = new Distance(20);
    }

    @DisplayName("길이 더하기")
    @Test
    void addDistance() {
        // when & then
        assertThat(길이_5.addDistance(길이_15)).isEqualTo(길이_20);
    }

    @DisplayName("길이 빼기")
    @Test
    void minusDistance() {
        // when & then
        assertThat(길이_20.minusDistance(길이_5)).isEqualTo(길이_15);
    }

    @DisplayName("음수값으로 길이 생성시 예외 발생")
    @ParameterizedTest
    @ValueSource(ints = { -10, -20 })
    void negativeDistance(int invalidValue) {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            Distance distance = new Distance(invalidValue);
        }).withMessageMatching("거리는 음수가 될 수 없습니다.");
    }
}
