package nextstep.subway.line.domain;

import nextstep.subway.line.exception.LineException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class ChargeTest {
    @Test
    @DisplayName("기본 요금 생성")
    void ofBasic() {
        // then
        assertThat(Charge.ofBasic().getValue()).isEqualTo(1250);
    }

    @Test
    @DisplayName("라인별 추가 요금 생성")
    void ofExtraCharge() {
        // then
        assertThat(Charge.ofExtraCharge(500).getValue()).isEqualTo(1750);
    }

    @Test
    @DisplayName("기본 요금 보다 작은 금액 입력 오류")
    void lessBasicCharge() {
        // then
        assertThatThrownBy(() -> new Charge(300)).isInstanceOf(LineException.class);
    }
}
