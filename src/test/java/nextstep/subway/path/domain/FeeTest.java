package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FeeTest {
    private Fee fee;

    @BeforeEach
    void setUp() {
        fee = Fee.defaultFee();
    }

    @Test
    @DisplayName("요금 객체가 같은지 검증")
    void verifySameFee() {
        assertThat(fee).isEqualTo(Fee.defaultFee());
    }

    @Test
    @DisplayName("더한 금액이 잘 반영되는지 검증")
    void addExtraFee() {
        fee.add(500);

        assertThat(fee.getFee()).isEqualTo(1750);
    }

    @Test
    @DisplayName("금액이 업데이트되는지 확인")
    void updateFee() {
        fee.update(500);

        assertThat(fee.getFee()).isEqualTo(500);
    }
}
