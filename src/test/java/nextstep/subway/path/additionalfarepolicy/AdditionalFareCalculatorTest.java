package nextstep.subway.path.additionalfarepolicy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.additionalfarepolicy.memberfarepolicy.NoneDiscountPolicy;
import nextstep.subway.path.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AdditionalFareCalculatorTest {
    @DisplayName("요금 추가 금액 계산")
    @ParameterizedTest()
    @CsvSource({"10, 1250",
            "30, 1650",
            "50, 2050",
            "58, 2150",
            "106, 2750"
    })
    void calculateOverFare(int 거리, int 요금) {
        // given
        Fare fare = new Fare();
        // when
        Fare totalFare = AdditionalFareCalculator.calculate(fare, new Distance(거리), new LoginMember());
        // then
        assertThat(totalFare.amount()).isEqualTo(요금);
    }
}