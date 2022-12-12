package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("나이별 추가 요금 계산")
public class DiscountPolicyByAgeTest {

    @DisplayName("청소년인 경우 운임에서 350원을 공제한 금액의 20% 할인된다.")
    @Test
    void 청소년_할인_정책() {
        Fare calculate = DiscountPolicyByAge.calculate(1350, 18);
        assertThat(calculate).isEqualTo(Fare.from(1150));
    }

    @DisplayName("어린이인 경우 운임에서 350원을 공제한 금액의 50% 할인된다.")
    @Test
    void 어린이_할인_정책() {
        Fare calculate = DiscountPolicyByAge.calculate(1350, 12);
        assertThat(calculate).isEqualTo(Fare.from(850));
    }
}
