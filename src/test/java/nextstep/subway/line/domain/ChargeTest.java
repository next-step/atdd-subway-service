package nextstep.subway.line.domain;

import nextstep.subway.line.exception.LineException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class ChargeTest {
    @Test
    @DisplayName("라인별 추가 요금 생성")
    void ofExtraCharge() {
        // then
        assertThat(ExtraCharge.of(500).getValue()).isEqualTo(500);
    }

    @Test
    @DisplayName("추가 요금 음수 오류")
    void lessBasicCharge() {
        // then
        assertThatThrownBy(() -> new ExtraCharge(-500)).isInstanceOf(LineException.class);
    }
}
