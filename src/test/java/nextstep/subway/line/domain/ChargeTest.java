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
    @DisplayName("기본 요금 보다 작은 금액 입력 오류")
    void lessBasicCharge() {
        // then
        assertThatThrownBy(() -> Charge.of(500)).isInstanceOf(LineException.class);
    }
}
