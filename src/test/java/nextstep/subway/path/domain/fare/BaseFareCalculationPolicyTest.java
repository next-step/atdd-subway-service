package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.fare.discount.AgeDiscountPolicy;
import nextstep.subway.path.domain.fare.discount.DiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BaseFareCalculationPolicyTest {
    @Test
    @DisplayName("기본 요금정책으로 연령별 요금을 계산한다: 유아")
    void calculateFareOfInfant() {
        DiscountPolicy 연령별_할인 = AgeDiscountPolicy.from(5);

        연령별_기본_요금정책_계산_성공(연령별_할인, 0);
    }

    @Test
    @DisplayName("기본 요금정책으로 연령별 요금을 계산한다: 어린이")
    void calculateFareOfChild() {
        DiscountPolicy 연령별_할인 = AgeDiscountPolicy.from(10);

        연령별_기본_요금정책_계산_성공(연령별_할인, 450);
    }

    @Test
    @DisplayName("기본 요금정책으로 연령별 요금을 계산한다: 청소년")
    void calculateFareOfAdolescent() {
        DiscountPolicy 연령별_할인 = AgeDiscountPolicy.from(15);

        연령별_기본_요금정책_계산_성공(연령별_할인, 720);
    }

    @Test
    @DisplayName("기본 요금정책으로 연령별 요금을 계산한다: 어른")
    void calculateFareOfAdult() {
        DiscountPolicy 연령별_할인 = AgeDiscountPolicy.from(20);

        연령별_기본_요금정책_계산_성공(연령별_할인, 1250);
    }

    private void 연령별_기본_요금정책_계산_성공(DiscountPolicy 연령별_할인, int 요금) {
        FareCalculationPolicy 기본_요금정책 = new BaseFareCalculationPolicy(연령별_할인);

        assertThat(기본_요금정책.calculateFare())
                .isEqualTo(요금);
    }
}
