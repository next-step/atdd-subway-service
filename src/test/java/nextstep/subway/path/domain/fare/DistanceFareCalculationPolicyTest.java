package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.fare.discount.AgeDiscountPolicy;
import nextstep.subway.path.domain.fare.discount.DiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFareCalculationPolicyTest {

    @ParameterizedTest
    @CsvSource(value = {"10:1250", "15:1350", "25:1550", "39:1850", "40:1850", "50:2050", "65:2250", "66:2250", "90:2550"}, delimiter = ':')
    @DisplayName("거리별 어른 요금을 계산한다.")
    void calculateFareOfAdultByDistance(int 거리, int 예상요금) {
        int 성인_연령 = 20;

        거리별_요금_계산_성공(성인_연령, 거리, 예상요금);
    }

    @ParameterizedTest
    @CsvSource(value = {"10:720", "15:800", "25:960", "39:1200", "40:1200", "50:1360", "65:1520", "66:1520", "90:1760"}, delimiter = ':')
    @DisplayName("거리별 청소년 요금을 계산한다.")
    void calculateFareOfAdolescentByDistance(int 거리, int 예상요금) {
        int 청소년_연령 = 15;

        거리별_요금_계산_성공(청소년_연령, 거리, 예상요금);
    }

    @ParameterizedTest
    @CsvSource(value = {"10:450", "15:500", "25:600", "39:750", "40:750", "50:850", "65:950", "66:950", "90:1100"}, delimiter = ':')
    @DisplayName("거리별 어린이 요금을 계산한다.")
    void calculateFareOfChildByDistance(int 거리, int 예상요금) {
        int 어린이_연령 = 10;

        거리별_요금_계산_성공(어린이_연령, 거리, 예상요금);
    }

    @ParameterizedTest
    @CsvSource(value = {"10:0", "15:0", "25:0", "40:0", "50:0", "66:0", "90:0"}, delimiter = ':')
    @DisplayName("거리별 유아 요금을 계산한다.")
    void calculateFareOfInfantByDistance(int 거리, int 예상요금) {
        int 유아_연령 = 5;

        거리별_요금_계산_성공(유아_연령, 거리, 예상요금);
    }

    private void 거리별_요금_계산_성공(int 연령, int 거리, int 예상요금) {
        DiscountPolicy 연령별_할인 = AgeDiscountPolicy.from(연령);
        FareCalculationPolicy distanceFareCalculator = new DistanceFareCalculationPolicy(연령별_할인, 거리);

        assertThat(distanceFareCalculator.calculateFare())
                .isEqualTo(예상요금);
    }
}
