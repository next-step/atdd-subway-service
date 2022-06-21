package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExtraChargeTest {

    @Test
    @DisplayName("추가요금 객체가 같은지 검증")
    void verifySameExtraCharge() {
        assertThat(ExtraCharge.of(10)).isEqualTo(ExtraCharge.of(10));
    }

    @Test
    @DisplayName("0 미만의 수 이면 오류 발생")
    void invalidInput() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> ExtraCharge.of(-4));
    }

    @Test
    @DisplayName("두 추가요금 중 큰 수를 반환")
    void maxExtraCharge() {
        final ExtraCharge ten = ExtraCharge.of(10);
        final ExtraCharge five = ExtraCharge.of(5);

        assertThat(ExtraCharge.max(ten, five)).isEqualTo(ten);
    }
}
