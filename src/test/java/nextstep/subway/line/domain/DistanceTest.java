package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceTest {

    @Test
    @DisplayName("객체가 같은지 검증")
    void verifySameDistance() {
        final Distance five = Distance.of(5);

        assertThat(five).isEqualTo(Distance.of(5));
    }

    @Test
    @DisplayName("올바르지 않는 값이 들어왔을 때 예외 발생")
    void inputInvalid() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Distance.of(-5))
                .withMessage("거리는 0이하가 될 수 없습니다.");
    }

    @Test
    @DisplayName("두 거리를 합한 값을 검증")
    void addDistance() {
        final Distance five = Distance.of(5);
        final Distance four = Distance.of(4);

        assertThat(five.add(four)).isEqualTo(Distance.of(9));
    }

    @Test
    @DisplayName("두 거리의 차인 값을 검증")
    void subtractDistance() {
        final Distance five = Distance.of(5);
        final Distance four = Distance.of(4);

        assertThat(five.subtract(four)).isEqualTo(Distance.of(1));
    }
}
