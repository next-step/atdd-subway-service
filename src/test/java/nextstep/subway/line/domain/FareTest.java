package nextstep.subway.line.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {
    @Test
    @DisplayName("생성 테스트")
    void create() {
        // given & when & then
        assertThat(Fare.init().toNumber()).isEqualTo(1250);
        assertThat(Fare.of(500).toNumber()).isEqualTo(1750);
    }

    @Test
    @DisplayName("생성 예외 테스트")
    void create_exception() {
        // given & when & then
        Assertions.assertThrows(IllegalArgumentException.class, () -> Fare.of(-1));
    }
}
