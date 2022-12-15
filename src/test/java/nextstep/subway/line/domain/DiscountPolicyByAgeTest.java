package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("나이별 추가 요금 계산")
public class DiscountPolicyByAgeTest {

    @DisplayName("청소년인 경우 운임에서 350원을 공제한 금액의 20% 할인된다.")
    @Test
    void 청소년_할인_정책() {
        Fare calculate = DiscountPolicyByAge.calculate(Fare.from(1350), 18);
        assertThat(calculate.value().intValue()).isEqualTo(1150);
    }

    @DisplayName("어린이인 경우 운임에서 350원을 공제한 금액의 50% 할인된다.")
    @Test
    void 어린이_할인_정책() {
        Fare calculate = DiscountPolicyByAge.calculate(Fare.from(1350), 12);
        assertThat(calculate.value().intValue()).isEqualTo(850);
    }

    @DisplayName("1세보다 작은 나이는 유효하지 않습니다.")
    @Test
    void 나이가_0세인_경우() {
        assertThatIllegalArgumentException().isThrownBy(() -> DiscountPolicyByAge.calculate(Fare.from(1350), 0));
    }

    @DisplayName("음수인 나이는 유효하지 않습니다.")
    @Test
    void 나이가_음수인_경우() {
        assertThatIllegalArgumentException().isThrownBy(() -> DiscountPolicyByAge.calculate(Fare.from(1350), -1));
    }

    @DisplayName("101세 이상은 유효하지 않습니다.")
    @Test
    void 나이가_101세인_경우() {
        assertThatIllegalArgumentException().isThrownBy(() -> DiscountPolicyByAge.calculate(Fare.from(1350), 101));
    }
}
