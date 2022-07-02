package nextstep.subway.path.domain.fare.discount;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class AgeDiscountPolicyTest {
    @Test
    @DisplayName("연령 정보가 잘못된 경우 할인 정책 생성에 실패한다.")
    void createDiscountPolicy() {
        int 잘못된_연령정보 = -10;

        assertThatThrownBy(() -> AgeDiscountPolicy.from(잘못된_연령정보))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("연령 정보를 확인해 주세요.");
    }

    @Test
    @DisplayName("연령에 따라 할인을 적용한다.")
    void discountByAge() {
        int 기본요금 = 1250;

        assertAll(
                () -> assertThat(AgeDiscountPolicy.from(5).discount(기본요금))
                        .isZero(),
                () -> assertThat(AgeDiscountPolicy.from(10).discount(기본요금))
                        .isEqualTo(450),
                () -> assertThat(AgeDiscountPolicy.from(18).discount(기본요금))
                        .isEqualTo(720),
                () -> assertThat(AgeDiscountPolicy.from(20).discount(기본요금))
                        .isEqualTo(1250)
        );
    }
}
