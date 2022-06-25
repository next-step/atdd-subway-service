package nextstep.subway.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AgeTest {

    @DisplayName("동등성 테스트")
    @Test
    void createTest() {
        assertThat(new Age(10)).isEqualTo(new Age(10));
    }

    @DisplayName("음수를 가질수 없다.")
    @Test
    void invalidCreateTest() {
        assertThatThrownBy(() -> new Age(-1)).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}