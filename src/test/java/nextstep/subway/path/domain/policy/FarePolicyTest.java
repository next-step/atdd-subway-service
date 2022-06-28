package nextstep.subway.path.domain.policy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class FarePolicyTest {

    FarePolicy farePolicy;

    @Test
    @DisplayName("일반요금_할인로직을_적용하면_적용된_금액이_최종금액이된다")
    void discountForAll() {
        //given
        farePolicy = new AllFarePolicy();

        //when
        int fare = farePolicy.discount(1500);

        //then
        assertThat(fare).isEqualTo(1500);
    }

    @Test
    @DisplayName("청소년요금_할인로직을_적용하면_적용된_금액이_최종금액이된다")
    void discountForTeenager() {
        //given
        farePolicy = new TeenagerFarePolicy();

        //when
        int fare = farePolicy.discount(1500);

        //then
        assertThat(fare).isEqualTo(920);
    }

    @Test
    @DisplayName("청소년요금_할인로직을_적용했을때_유효하지_않은_금액이_나오면_에러")
    void discountForTeenagerMinusException() {
        //given
        farePolicy = new TeenagerFarePolicy();

        //then
        assertThatThrownBy(() -> farePolicy.discount(300)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("아동요금_할인로직을_적용하면_적용된_금액이_최종금액이된다")
    void discountForChildren() {
        //given
        farePolicy = new ChildrenFarePolicy();

        //when
        int fare = farePolicy.discount(1500);

        //then
        assertThat(fare).isEqualTo(575);
    }

    @Test
    @DisplayName("아동요금_할인로직을_적용했을때_유효하지_않은_금액이_나오면_에러")
    void discountForChildrenMinusException() {
        //given
        farePolicy = new ChildrenFarePolicy();

        //then
        assertThatThrownBy(() -> farePolicy.discount(300)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("무료요금대상자_할인로직을_적용하면_적용된_금액이_최종금액이된다")
    void discountForFree() {
        //given
        farePolicy = new FreeFarePolicy();

        //when
        int fare = farePolicy.discount(1500);

        //then
        assertThat(fare).isZero();
    }
}