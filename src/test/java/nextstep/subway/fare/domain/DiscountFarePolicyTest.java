package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("할인 정책 단위테스트")
class DiscountFarePolicyTest {
    
    @DisplayName("청소년인 경우 운임에서 350원을 공제한 금액의 20% 할인된다.")
    @Test
    void 청소년_할인_정책() {
        Fare 청소년 = DiscountFarePolicy.calculate(Fare.from(1350), 18);
        Fare 성인 = DiscountFarePolicy.calculate(Fare.from(1350), 19);
        assertThat(청소년.value()).isEqualTo(1150);
        assertThat(성인.value()).isEqualTo(1350);
    }

    @DisplayName("어린이인 경우 운임에서 350원을 공제한 금액의 50% 할인된다.")
    @Test
    void 어린이_할인_정책() {
        Fare 어린이 = DiscountFarePolicy.calculate(Fare.from(1350), 12);
        Fare 청소년 = DiscountFarePolicy.calculate(Fare.from(1350), 13);
        assertThat(어린이.value()).isEqualTo(850);
        assertThat(청소년.value()).isEqualTo(1150);
    }

    @DisplayName("1세보다 작은 나이는 유효하지 않습니다.")
    @Test
    void 나이가_0세인_경우() {
        assertThatIllegalArgumentException().isThrownBy(() -> DiscountFarePolicy.calculate(Fare.from(1350), 0));
    }
}
