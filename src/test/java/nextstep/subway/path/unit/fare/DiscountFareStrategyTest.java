package nextstep.subway.path.unit.fare;

import static org.junit.jupiter.api.Assertions.*;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.fare.DiscountFareStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DiscountFareStrategyTest {

    @DisplayName("연령별 할인요금 조회")
    @ParameterizedTest
    @CsvSource(value = {"10:500", "15:800", "20:1000"}, delimiter = ':')
    void getDiscountedFareByAge(int ageInput, int expectedFare) {
        // given
        LoginMember loginMember = new LoginMember(1L, "email@email.com", ageInput);
        DiscountFareStrategy discountFareStrategy = new DiscountFareStrategy(loginMember);

        // when
        int discountedFare = discountFareStrategy.getDiscountedFare(1350);

        // then
        assertEquals(expectedFare, discountedFare);
    }
}