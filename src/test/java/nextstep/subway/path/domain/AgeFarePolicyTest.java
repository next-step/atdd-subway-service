package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AgeFarePolicyTest {
    @Test
    @DisplayName("연령에 따른 할인금액 계산")
    void calculate() {
        // given & when & then
        assertAll(
                () -> assertThat(AgeFarePolicy.find(5).calculate(1250)).isEqualTo(1250),
                () -> assertThat(AgeFarePolicy.find(6).calculate(1250)).isEqualTo(450),
                () -> assertThat(AgeFarePolicy.find(12).calculate(1250)).isEqualTo(450),
                () -> assertThat(AgeFarePolicy.find(18).calculate(1250)).isEqualTo(720),
                () -> assertThat(AgeFarePolicy.find(19).calculate(1250)).isEqualTo(1250)
        );
    }
}
