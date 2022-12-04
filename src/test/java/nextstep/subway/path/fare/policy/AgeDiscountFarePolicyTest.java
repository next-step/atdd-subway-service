package nextstep.subway.path.fare.policy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.fare.Fare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeDiscountFarePolicyTest {

    AgeDiscountFarePolicy discountFarePolicy;

    @BeforeEach
    void setUp() {
        discountFarePolicy = new AgeDiscountFarePolicy();
    }

    @ParameterizedTest
    @CsvSource({"13,1250,720", "18,1250,720", "18,2550,1760"})
    void testTeenagersDiscountFarePolicy(int age, int originalFare, int expectedDiscountFare) {
        LoginMember loginMember = new LoginMember(0L, "", age);
        Fare actualDiscountAmount = discountFarePolicy.discount(loginMember, Fare.valueOf(originalFare));

        assertThat(actualDiscountAmount).isEqualTo(Fare.valueOf(expectedDiscountFare));
    }

    @ParameterizedTest
    @CsvSource({"12,1250,450", "6,2750,1200"})
    void testChildrenDiscountFarePolicy(int age, int originalFare, int expectedDiscountFare) {
        LoginMember loginMember = new LoginMember(0L, "", age);
        Fare actualDiscountFare = discountFarePolicy.discount(loginMember, Fare.valueOf(originalFare));

        assertThat(actualDiscountFare).isEqualTo(Fare.valueOf(expectedDiscountFare));
    }

}
